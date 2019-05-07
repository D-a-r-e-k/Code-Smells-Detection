static String GetStateSetString(int[] states) {
    String retVal = "{ ";
    for (int i = 0; i < states.length; ) {
        retVal += states[i] + ", ";
        if (i++ > 0 && i % 16 == 0)
            retVal += "\n";
    }
    retVal += "};";
    allNextStates.put(retVal, states);
    return retVal;
}
