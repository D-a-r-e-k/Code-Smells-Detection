public void meetInto(IsNullValueFrame fact, Edge edge, IsNullValueFrame result, boolean propagatePhiNodeInformation) throws DataflowAnalysisException {
    if (fact.isValid()) {
        IsNullValueFrame tmpFact = null;
        if (!NO_SPLIT_DOWNGRADE_NSP) {
            // Downgrade NSP to DNR on non-exception control splits 
            if (!edge.isExceptionEdge() && cfg.getNumNonExceptionSucessors(edge.getSource()) > 1) {
                tmpFact = modifyFrame(fact, tmpFact);
                tmpFact.downgradeOnControlSplit();
            }
        }
        if (!NO_SWITCH_DEFAULT_AS_EXCEPTION) {
            if (edge.getType() == SWITCH_DEFAULT_EDGE) {
                tmpFact = modifyFrame(fact, tmpFact);
                tmpFact.toExceptionValues();
            }
        }
        final BasicBlock destBlock = edge.getTarget();
        if (destBlock.isExceptionHandler()) {
            // Exception handler - clear stack and push a non-null value 
            // to represent the exception. 
            tmpFact = modifyFrame(fact, tmpFact);
            tmpFact.clearStack();
            // Downgrade NULL and NSP to DNR if the handler is for 
            // CloneNotSupportedException or InterruptedException 
            if (true) {
                CodeExceptionGen handler = destBlock.getExceptionGen();
                ObjectType catchType = handler.getCatchType();
                if (catchType != null) {
                    String catchClass = catchType.getClassName();
                    if (catchClass.equals("java.lang.CloneNotSupportedException") || catchClass.equals("java.lang.InterruptedException")) {
                        for (int i = 0; i < tmpFact.getNumSlots(); ++i) {
                            IsNullValue value = tmpFact.getValue(i);
                            if (value.isDefinitelyNull() || value.isNullOnSomePath())
                                tmpFact.setValue(i, IsNullValue.nullOnComplexPathValue());
                        }
                    }
                }
            }
            // Mark all values as having occurred on an exception path 
            tmpFact.toExceptionValues();
            // Push the exception value 
            tmpFact.pushValue(IsNullValue.nonNullValue());
        } else {
            final int edgeType = edge.getType();
            final BasicBlock sourceBlock = edge.getSource();
            final BasicBlock targetBlock = edge.getTarget();
            final ValueNumberFrame targetVnaFrame = vnaDataflow.getStartFact(destBlock);
            final ValueNumberFrame sourceVnaFrame = vnaDataflow.getResultFact(sourceBlock);
            assert targetVnaFrame != null;
            // Determine if the edge conveys any information about the 
            // null/non-null status of operands in the incoming frame. 
            if (edgeType == IFCMP_EDGE || edgeType == FALL_THROUGH_EDGE) {
                IsNullValueFrame resultFact = getResultFact(sourceBlock);
                IsNullConditionDecision decision = resultFact.getDecision();
                if (decision != null) {
                    if (!decision.isEdgeFeasible(edgeType)) {
                        // The incoming edge is infeasible; just use TOP 
                        // as the start fact for this block. 
                        tmpFact = createFact();
                        tmpFact.setTop();
                    } else {
                        ValueNumber valueTested = decision.getValue();
                        if (valueTested != null) {
                            // A value has been determined for this edge. 
                            // Use the value to update the is-null information in 
                            // the start fact for this block. 
                            if (DEBUG) {
                                System.out.println("Updating edge information for " + valueTested);
                            }
                            final Location atIf = new Location(sourceBlock.getLastInstruction(), sourceBlock);
                            final ValueNumberFrame prevVnaFrame = vnaDataflow.getFactAtLocation(atIf);
                            IsNullValue decisionValue = decision.getDecision(edgeType);
                            if (decisionValue != null) {
                                if (DEBUG) {
                                    System.out.println("Set decision information");
                                    System.out.println("  " + valueTested + " becomes " + decisionValue);
                                    System.out.println("  at " + targetBlock.getFirstInstruction().getPosition());
                                    System.out.println("  prev available loads: " + prevVnaFrame.availableLoadMapAsString());
                                    System.out.println("  target available loads: " + targetVnaFrame.availableLoadMapAsString());
                                }
                                tmpFact = replaceValues(fact, tmpFact, valueTested, prevVnaFrame, targetVnaFrame, decisionValue);
                                if (decisionValue.isDefinitelyNull()) {
                                    // Make a note of the value that has become null 
                                    // due to the if comparison. 
                                    addLocationWhereValueBecomesNull(new LocationWhereValueBecomesNull(atIf, valueTested));
                                    ValueNumber knownNonnull = getKnownNonnullDueToPointerDisequality(valueTested, atIf.getHandle().getPosition());
                                    if (knownNonnull != null)
                                        tmpFact = replaceValues(fact, tmpFact, knownNonnull, prevVnaFrame, targetVnaFrame, IsNullValue.checkedNonNullValue());
                                }
                            }
                        }
                    }
                }
            }
            // if (edgeType == IFCMP_EDGE || edgeType == FALL_THROUGH_EDGE) 
            // If this is a fall-through edge from a null check, 
            // then we know the value checked is not null. 
            if (sourceBlock.isNullCheck() && edgeType == FALL_THROUGH_EDGE) {
                ValueNumberFrame vnaFrame = vnaDataflow.getStartFact(destBlock);
                if (vnaFrame == null)
                    throw new IllegalStateException("no vna frame at block entry?");
                Instruction firstInDest = edge.getTarget().getFirstInstruction().getInstruction();
                IsNullValue instance = fact.getInstance(firstInDest, methodGen.getConstantPool());
                if (instance.isDefinitelyNull()) {
                    // If we know the variable is null, this edge is infeasible 
                    tmpFact = createFact();
                    tmpFact.setTop();
                } else if (!instance.isDefinitelyNotNull()) {
                    // If we're not sure that the instance is definitely non-null, 
                    // update the is-null information for the dereferenced value. 
                    InstructionHandle kaBoomLocation = targetBlock.getFirstInstruction();
                    ValueNumber replaceMe = vnaFrame.getInstance(firstInDest, methodGen.getConstantPool());
                    IsNullValue noKaboomNonNullValue = IsNullValue.noKaboomNonNullValue(new Location(kaBoomLocation, targetBlock));
                    if (DEBUG) {
                        System.out.println("Start vna fact: " + vnaFrame);
                        System.out.println("inva fact: " + fact);
                        System.out.println("\nGenerated NoKaboom value for location " + kaBoomLocation);
                        System.out.println("Dereferenced " + instance);
                        System.out.println("On fall through from source block " + sourceBlock);
                    }
                    tmpFact = replaceValues(fact, tmpFact, replaceMe, vnaFrame, targetVnaFrame, noKaboomNonNullValue);
                }
            }
            // if (sourceBlock.isNullCheck() && edgeType == FALL_THROUGH_EDGE) 
            if (propagatePhiNodeInformation && targetVnaFrame.phiNodeForLoads) {
                if (DEBUG)
                    System.out.println("Is phi node for loads");
                for (ValueNumber v : fact.getKnownValues()) {
                    AvailableLoad loadForV = sourceVnaFrame.getLoad(v);
                    if (DEBUG) {
                        System.out.println("  " + v + " for " + loadForV);
                    }
                    if (loadForV != null) {
                        ValueNumber[] matchingValueNumbers = targetVnaFrame.getAvailableLoad(loadForV);
                        if (matchingValueNumbers != null)
                            for (ValueNumber v2 : matchingValueNumbers) {
                                tmpFact = modifyFrame(fact, tmpFact);
                                tmpFact.useNewValueNumberForLoad(v, v2);
                                if (DEBUG)
                                    System.out.println("For " + loadForV + " switch from " + v + " to " + v2);
                            }
                    }
                }
            }
        }
        if (tmpFact != null)
            fact = tmpFact;
    }
    // if (fact.isValid()) 
    if (DEBUG) {
        System.out.println("At " + edge);
        System.out.println("Merge " + fact + " into " + result);
    }
    // Normal dataflow merge 
    mergeInto(fact, result);
    if (DEBUG) {
        System.out.println("getting " + result);
    }
}
