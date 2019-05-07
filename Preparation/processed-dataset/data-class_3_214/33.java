/**
     * Initializes the data structures for the variables, stack, etc.
     */
private void initializeParameters(Clazz clazz, Method method, CodeAttribute codeAttribute, TracedVariables variables) {
    // Create the method parameters. 
    TracedVariables parameters = new TracedVariables(codeAttribute.u2maxLocals);
    // Remember this instruction's offset with any stored value. 
    Value storeValue = new InstructionOffsetValue(AT_METHOD_ENTRY);
    parameters.setProducerValue(storeValue);
    // Initialize the method parameters. 
    invocationUnit.enterMethod(clazz, method, parameters);
    if (DEBUG) {
        System.out.println("  Params: " + parameters);
    }
    // Initialize the variables with the parameters. 
    variables.initialize(parameters);
    // Set the store value of each parameter variable. 
    InstructionOffsetValue atMethodEntry = new InstructionOffsetValue(AT_METHOD_ENTRY);
    for (int index = 0; index < parameters.size(); index++) {
        variables.setProducerValue(index, atMethodEntry);
    }
}
