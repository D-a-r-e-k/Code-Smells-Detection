/**
     * Marks the specified instruction if it is a required dup/swap instruction,
     * replacing it by an appropriate variant if necessary.
     * @param clazz         the class that is being checked.
     * @param codeAttribute the code that is being checked.
     * @param dupOffset     the offset of the dup/swap instruction.
     * @param instruction   the dup/swap instruction.
     */
private void fixDupInstruction(Clazz clazz, CodeAttribute codeAttribute, int dupOffset, Instruction instruction) {
    int top = partialEvaluator.getStackAfter(dupOffset).size() - 1;
    byte oldOpcode = instruction.opcode;
    byte newOpcode = 0;
    // Simplify the popping instruction if possible. 
    switch(oldOpcode) {
        case InstructionConstants.OP_DUP:
            {
                boolean stackEntryPresent0 = isStackEntryNecessaryAfter(dupOffset, top - 0);
                boolean stackEntryPresent1 = isStackEntryNecessaryAfter(dupOffset, top - 1);
                // Should either the original element or the copy be present? 
                if (stackEntryPresent0 || stackEntryPresent1) {
                    // Should both the original element and the copy be present? 
                    if (stackEntryPresent0 && stackEntryPresent1) {
                        newOpcode = InstructionConstants.OP_DUP;
                    }
                }
                break;
            }
        case InstructionConstants.OP_DUP_X1:
            {
                boolean stackEntryPresent0 = isStackEntryNecessaryAfter(dupOffset, top - 0);
                boolean stackEntryPresent1 = isStackEntryNecessaryAfter(dupOffset, top - 1);
                boolean stackEntryPresent2 = isStackEntryNecessaryAfter(dupOffset, top - 2);
                // Should either the original element or the copy be present? 
                if (stackEntryPresent0 || stackEntryPresent2) {
                    // Should the copy be present? 
                    if (stackEntryPresent2) {
                        // Compute the number of elements to be skipped. 
                        int skipCount = stackEntryPresent1 ? 1 : 0;
                        // Should the original element be present? 
                        if (stackEntryPresent0) {
                            // Copy the original element. 
                            newOpcode = (byte) (InstructionConstants.OP_DUP + skipCount);
                        } else if (skipCount == 1) {
                            // Move the original element. 
                            newOpcode = InstructionConstants.OP_SWAP;
                        }
                    }
                }
                break;
            }
        case InstructionConstants.OP_DUP_X2:
            {
                boolean stackEntryPresent0 = isStackEntryNecessaryAfter(dupOffset, top - 0);
                boolean stackEntryPresent1 = isStackEntryNecessaryAfter(dupOffset, top - 1);
                boolean stackEntryPresent2 = isStackEntryNecessaryAfter(dupOffset, top - 2);
                boolean stackEntryPresent3 = isStackEntryNecessaryAfter(dupOffset, top - 3);
                // Should either the original element or the copy be present? 
                if (stackEntryPresent0 || stackEntryPresent3) {
                    // Should the copy be present? 
                    if (stackEntryPresent3) {
                        int skipCount = (stackEntryPresent1 ? 1 : 0) + (stackEntryPresent2 ? 1 : 0);
                        // Should the original element be present? 
                        if (stackEntryPresent0) {
                            // Copy the original element. 
                            newOpcode = (byte) (InstructionConstants.OP_DUP + skipCount);
                        } else if (skipCount == 1) {
                            // Move the original element. 
                            newOpcode = InstructionConstants.OP_SWAP;
                        } else if (skipCount == 2) {
                            // We can't easily move the original element. 
                            throw new UnsupportedOperationException("Can't handle dup_x2 instruction moving original element across two elements at [" + dupOffset + "]");
                        }
                    }
                }
                break;
            }
        case InstructionConstants.OP_DUP2:
            {
                boolean stackEntriesPresent01 = isStackEntriesNecessaryAfter(dupOffset, top - 0, top - 1);
                boolean stackEntriesPresent23 = isStackEntriesNecessaryAfter(dupOffset, top - 2, top - 3);
                // Should either the original element or the copy be present? 
                if (stackEntriesPresent01 || stackEntriesPresent23) {
                    // Should both the original element and the copy be present? 
                    if (stackEntriesPresent01 && stackEntriesPresent23) {
                        newOpcode = InstructionConstants.OP_DUP2;
                    }
                }
                break;
            }
        case InstructionConstants.OP_DUP2_X1:
            {
                boolean stackEntriesPresent01 = isStackEntriesNecessaryAfter(dupOffset, top - 0, top - 1);
                boolean stackEntryPresent2 = isStackEntryNecessaryAfter(dupOffset, top - 2);
                boolean stackEntriesPresent34 = isStackEntriesNecessaryAfter(dupOffset, top - 3, top - 4);
                // Should either the original element or the copy be present? 
                if (stackEntriesPresent01 || stackEntriesPresent34) {
                    // Should the copy be present? 
                    if (stackEntriesPresent34) {
                        int skipCount = stackEntryPresent2 ? 1 : 0;
                        // Should the original element be present? 
                        if (stackEntriesPresent01) {
                            // Copy the original element. 
                            newOpcode = (byte) (InstructionConstants.OP_DUP2 + skipCount);
                        } else if (skipCount > 0) {
                            // We can't easily move the original element. 
                            throw new UnsupportedOperationException("Can't handle dup2_x1 instruction moving original element across " + skipCount + " elements at [" + dupOffset + "]");
                        }
                    }
                }
                break;
            }
        case InstructionConstants.OP_DUP2_X2:
            {
                boolean stackEntriesPresent01 = isStackEntriesNecessaryAfter(dupOffset, top - 0, top - 1);
                boolean stackEntryPresent2 = isStackEntryNecessaryAfter(dupOffset, top - 2);
                boolean stackEntryPresent3 = isStackEntryNecessaryAfter(dupOffset, top - 3);
                boolean stackEntriesPresent45 = isStackEntriesNecessaryAfter(dupOffset, top - 4, top - 5);
                // Should either the original element or the copy be present? 
                if (stackEntriesPresent01 || stackEntriesPresent45) {
                    // Should the copy be present? 
                    if (stackEntriesPresent45) {
                        int skipCount = (stackEntryPresent2 ? 1 : 0) + (stackEntryPresent3 ? 1 : 0);
                        // Should the original element be present? 
                        if (stackEntriesPresent01) {
                            // Copy the original element. 
                            newOpcode = (byte) (InstructionConstants.OP_DUP2 + skipCount);
                        } else if (skipCount > 0) {
                            // We can't easily move the original element. 
                            throw new UnsupportedOperationException("Can't handle dup2_x2 instruction moving original element across " + skipCount + " elements at [" + dupOffset + "]");
                        }
                    }
                }
                break;
            }
        case InstructionConstants.OP_SWAP:
            {
                boolean stackEntryPresent0 = isStackEntryNecessaryAfter(dupOffset, top - 0);
                boolean stackEntryPresent1 = isStackEntryNecessaryAfter(dupOffset, top - 1);
                // Will either element be present? 
                if (stackEntryPresent0 || stackEntryPresent1) {
                    // Will both elements be present? 
                    if (stackEntryPresent0 && stackEntryPresent1) {
                        newOpcode = InstructionConstants.OP_SWAP;
                    }
                }
                break;
            }
    }
    if (newOpcode == 0) {
        // Delete the instruction. 
        codeAttributeEditor.deleteInstruction(dupOffset);
        if (extraDeletedInstructionVisitor != null) {
            extraDeletedInstructionVisitor.visitSimpleInstruction(null, null, null, dupOffset, null);
        }
        if (DEBUG)
            System.out.println("  Marking but deleting instruction " + instruction.toString(dupOffset));
    } else if (newOpcode == oldOpcode) {
        // Leave the instruction unchanged. 
        codeAttributeEditor.undeleteInstruction(dupOffset);
        if (DEBUG)
            System.out.println("  Marking unchanged instruction " + instruction.toString(dupOffset));
    } else {
        // Replace the instruction. 
        Instruction replacementInstruction = new SimpleInstruction(newOpcode);
        codeAttributeEditor.replaceInstruction(dupOffset, replacementInstruction);
        if (DEBUG)
            System.out.println("  Replacing instruction " + instruction.toString(dupOffset) + " by " + replacementInstruction.toString());
    }
}
