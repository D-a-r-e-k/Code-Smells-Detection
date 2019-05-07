private static void ReArrange() {
    List v = allStates;
    allStates = new ArrayList(Collections.nCopies(generatedStates, null));
    if (allStates.size() != generatedStates)
        throw new Error("What??");
    for (int j = 0; j < v.size(); j++) {
        NfaState tmp = (NfaState) v.get(j);
        if (tmp.stateName != -1 && !tmp.dummy)
            allStates.set(tmp.stateName, tmp);
    }
}
