/**
     * Replaces the instruction at a given offset by a static invocation.
     */
private void replaceByStaticInvocation(Clazz clazz, int offset, ConstantInstruction constantInstruction) {
    // Remember the replacement instruction. 
    Instruction replacementInstruction = new ConstantInstruction(InstructionConstants.OP_INVOKESTATIC, constantInstruction.constantIndex).shrink();
    if (DEBUG)
        System.out.println("  Replacing by static invocation " + constantInstruction.toString(offset) + " -> " + replacementInstruction.toString());
    codeAttributeEditor.replaceInstruction(offset, replacementInstruction);
}
