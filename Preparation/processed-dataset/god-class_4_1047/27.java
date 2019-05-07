/**
     * Returns whether the specified variable must be initialized at the
     * specified offset, according to the verifier of the JVM.
     */
private boolean isVariableInitializationNecessary(Clazz clazz, Method method, CodeAttribute codeAttribute, int initializationOffset, int variableIndex) {
    int codeLength = codeAttribute.u4codeLength;
    // Is the variable necessary anywhere at all? 
    if (isVariableNecessaryAfterAny(0, codeLength, variableIndex)) {
        if (DEBUG)
            System.out.println("Simple partial evaluation for initialization of variable v" + variableIndex + " at [" + initializationOffset + "]");
        // Lazily perform simple partial evaluation, the way the JVM 
        // verifier would do it. 
        simplePartialEvaluator.visitCodeAttribute(clazz, method, codeAttribute);
        if (DEBUG)
            System.out.println("End of simple partial evaluation for initialization of variable v" + variableIndex + " at [" + initializationOffset + "]");
        // Check if the variable is necessary elsewhere. 
        for (int offset = 0; offset < codeLength; offset++) {
            if (partialEvaluator.isTraced(offset)) {
                Value producer = partialEvaluator.getVariablesBefore(offset).getProducerValue(variableIndex);
                if (producer != null) {
                    Value simpleProducer = simplePartialEvaluator.getVariablesBefore(offset).getProducerValue(variableIndex);
                    if (simpleProducer != null) {
                        InstructionOffsetValue producerOffsets = producer.instructionOffsetValue();
                        InstructionOffsetValue simpleProducerOffsets = simpleProducer.instructionOffsetValue();
                        if (DEBUG) {
                            System.out.println("  [" + offset + "] producers [" + producerOffsets + "], simple producers [" + simpleProducerOffsets + "]");
                        }
                        // Is the variable being used without all of its 
                        // immediate simple producers being marked? 
                        if (isVariableNecessaryAfterAny(producerOffsets, variableIndex) && !isVariableNecessaryAfterAll(simpleProducerOffsets, variableIndex)) {
                            if (DEBUG) {
                                System.out.println("    => initialization of variable v" + variableIndex + " at [" + initializationOffset + "] necessary");
                            }
                            // Then the initialization may be necessary. 
                            return true;
                        }
                    }
                }
            }
        }
    }
    if (DEBUG) {
        System.out.println("    => initialization of variable v" + variableIndex + " at [" + initializationOffset + "] not necessary");
    }
    return false;
}
