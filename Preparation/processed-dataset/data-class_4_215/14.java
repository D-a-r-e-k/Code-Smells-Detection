/**
     * Pushes a specified type of stack entry before or at the given offset.
     * The instruction is marked as necessary.
     */
private void insertPushInstructions(int offset, boolean replace, int computationalType) {
    // Mark this instruction. 
    markInstruction(offset);
    // Create a simple push instrucion. 
    Instruction replacementInstruction = new SimpleInstruction(pushOpcode(computationalType));
    if (DEBUG)
        System.out.println(": " + replacementInstruction.toString(offset));
    // Replace or insert the push instruction. 
    if (replace) {
        // Replace the push instruction. 
        codeAttributeEditor.replaceInstruction(offset, replacementInstruction);
    } else {
        // Insert the push instruction. 
        codeAttributeEditor.insertBeforeInstruction(offset, replacementInstruction);
        if (extraAddedInstructionVisitor != null) {
            replacementInstruction.accept(null, null, null, offset, extraAddedInstructionVisitor);
        }
    }
}
