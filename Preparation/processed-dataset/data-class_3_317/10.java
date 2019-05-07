/**
     * -1 is used to represent bad transitions in the transition table
     * entry for each state. So each entry is initialized to an all -1
     * array. This method creates a new entry and initializes it.
     */
private int[] makeDefStateList() {
    int[] retArray = new int[fElemMapSize];
    for (int index = 0; index < fElemMapSize; index++) retArray[index] = -1;
    return retArray;
}
