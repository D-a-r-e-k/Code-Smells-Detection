static String GetStateSetString(List states) {
    if (states == null || states.size() == 0)
        return "null;";
    int[] set = new int[states.size()];
    String retVal = "{ ";
    for (int i = 0; i < states.size(); ) {
        int k;
        retVal += (k = ((NfaState) states.get(i)).stateName) + ", ";
        set[i] = k;
        if (i++ > 0 && i % 16 == 0)
            retVal += "\n";
    }
    retVal += "};";
    allNextStates.put(retVal, set);
    return retVal;
}
