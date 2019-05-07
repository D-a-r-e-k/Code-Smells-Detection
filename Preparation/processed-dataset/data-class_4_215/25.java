/**
     * Returns whether the given stack entry is present after execution of the
     * instruction at the given offset.
     */
private boolean isStackEntriesNecessaryAfter(int instructionOffset, int stackIndex1, int stackIndex2) {
    boolean present1 = isStackEntryNecessaryAfter(instructionOffset, stackIndex1);
    boolean present2 = isStackEntryNecessaryAfter(instructionOffset, stackIndex2);
    //        if (present1 ^ present2) 
    //        { 
    //            throw new UnsupportedOperationException("Can't handle partial use of dup2 instructions"); 
    //        } 
    return present1 || present2;
}
