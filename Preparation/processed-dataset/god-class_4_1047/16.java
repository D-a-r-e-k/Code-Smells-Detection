/**
     * Pops the given number of stack entries at or after the given offset.
     * The instructions are marked as necessary.
     */
private void insertPopInstructions(int offset, boolean replace, int popCount) {
    // Mark this instruction. 
    markInstruction(offset);
    switch(popCount) {
        case 1:
            {
                // Replace or insert a single pop instruction. 
                Instruction popInstruction = new SimpleInstruction(InstructionConstants.OP_POP);
                if (replace) {
                    codeAttributeEditor.replaceInstruction(offset, popInstruction);
                } else {
                    codeAttributeEditor.insertAfterInstruction(offset, popInstruction);
                    if (extraAddedInstructionVisitor != null) {
                        popInstruction.accept(null, null, null, offset, extraAddedInstructionVisitor);
                    }
                }
                break;
            }
        case 2:
            {
                // Replace or insert a single pop2 instruction. 
                Instruction popInstruction = new SimpleInstruction(InstructionConstants.OP_POP2);
                if (replace) {
                    codeAttributeEditor.replaceInstruction(offset, popInstruction);
                } else {
                    codeAttributeEditor.insertAfterInstruction(offset, popInstruction);
                    if (extraAddedInstructionVisitor != null) {
                        popInstruction.accept(null, null, null, offset, extraAddedInstructionVisitor);
                    }
                }
                break;
            }
        default:
            {
                // Replace or insert the specified number of pop instructions. 
                Instruction[] popInstructions = new Instruction[popCount / 2 + popCount % 2];
                Instruction popInstruction = new SimpleInstruction(InstructionConstants.OP_POP2);
                for (int index = 0; index < popCount / 2; index++) {
                    popInstructions[index] = popInstruction;
                }
                if (popCount % 2 == 1) {
                    popInstruction = new SimpleInstruction(InstructionConstants.OP_POP);
                    popInstructions[popCount / 2] = popInstruction;
                }
                if (replace) {
                    codeAttributeEditor.replaceInstruction(offset, popInstructions);
                    for (int index = 1; index < popInstructions.length; index++) {
                        if (extraAddedInstructionVisitor != null) {
                            popInstructions[index].accept(null, null, null, offset, extraAddedInstructionVisitor);
                        }
                    }
                } else {
                    codeAttributeEditor.insertAfterInstruction(offset, popInstructions);
                    for (int index = 0; index < popInstructions.length; index++) {
                        if (extraAddedInstructionVisitor != null) {
                            popInstructions[index].accept(null, null, null, offset, extraAddedInstructionVisitor);
                        }
                    }
                }
                break;
            }
    }
}
