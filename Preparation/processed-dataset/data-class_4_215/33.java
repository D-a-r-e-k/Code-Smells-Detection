private void markStackEntryAfter(int instructionOffset, int stackIndex) {
    if (!isStackEntryNecessaryAfter(instructionOffset, stackIndex)) {
        if (DEBUG)
            System.out.print("[" + instructionOffset + ".s" + stackIndex + "],");
        stacksNecessaryAfter[instructionOffset][stackIndex] = true;
        if (maxMarkedOffset < instructionOffset) {
            maxMarkedOffset = instructionOffset;
        }
    }
}
