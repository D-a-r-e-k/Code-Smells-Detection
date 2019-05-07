/**
     * Replaces the given instruction by an infinite loop.
     */
private void replaceByInfiniteLoop(Clazz clazz, int offset) {
    if (DEBUG)
        System.out.println("  Inserting infinite loop at [" + offset + "]");
    // Mark the instruction. 
    markInstruction(offset);
    // Replace the instruction by an infinite loop. 
    Instruction replacementInstruction = new BranchInstruction(InstructionConstants.OP_GOTO, 0);
    codeAttributeEditor.replaceInstruction(offset, replacementInstruction);
}
