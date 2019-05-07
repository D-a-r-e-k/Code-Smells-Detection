public void endOfGroup(String value, int groupNumber) {
    if (deepestResetGroup > groupNumber) {
        deepestResetGroup = groupNumber;
    }
}
