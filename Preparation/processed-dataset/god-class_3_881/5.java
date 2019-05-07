/** 
     * Whether this instruction is target of branch instruction or
     * starts catch block.
     * 
     * @param ih Handle on instruction.
     * @return   True if targeted by branch or CodeExceptionGen.
     */
public static final boolean hasInbound(InstructionHandle ih) {
    if (ih.hasTargeters()) {
        InstructionTargeter targeters[] = ih.getTargeters();
        for (int j = 0; j < targeters.length; j++) {
            if (targeters[j] instanceof BranchInstruction) {
                return true;
            }
            if (targeters[j] instanceof CodeExceptionGen) {
                return true;
            }
        }
    }
    return false;
}
