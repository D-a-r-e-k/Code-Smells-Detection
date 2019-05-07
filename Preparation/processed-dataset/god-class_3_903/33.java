private static int AddCompositeStateSet(String stateSetString, boolean starts) {
    Integer stateNameToReturn;
    if ((stateNameToReturn = (Integer) stateNameForComposite.get(stateSetString)) != null)
        return stateNameToReturn.intValue();
    int toRet = 0;
    int[] nameSet = (int[]) allNextStates.get(stateSetString);
    if (!starts)
        stateBlockTable.put(stateSetString, stateSetString);
    if (nameSet == null)
        throw new Error("JavaCC Bug: Please send mail to sankar@cs.stanford.edu; nameSet null for : " + stateSetString);
    if (nameSet.length == 1) {
        stateNameToReturn = new Integer(nameSet[0]);
        stateNameForComposite.put(stateSetString, stateNameToReturn);
        return nameSet[0];
    }
    for (int i = 0; i < nameSet.length; i++) {
        if (nameSet[i] == -1)
            continue;
        NfaState st = (NfaState) indexedAllStates.get(nameSet[i]);
        st.isComposite = true;
        st.compositeStates = nameSet;
    }
    while (toRet < nameSet.length && (starts && ((NfaState) indexedAllStates.get(nameSet[toRet])).inNextOf > 1)) toRet++;
    Enumeration e = compositeStateTable.keys();
    String s;
    while (e.hasMoreElements()) {
        s = (String) e.nextElement();
        if (!s.equals(stateSetString) && Intersect(stateSetString, s)) {
            int[] other = (int[]) compositeStateTable.get(s);
            while (toRet < nameSet.length && ((starts && ((NfaState) indexedAllStates.get(nameSet[toRet])).inNextOf > 1) || ElemOccurs(nameSet[toRet], other) >= 0)) toRet++;
        }
    }
    int tmp;
    if (toRet >= nameSet.length) {
        if (dummyStateIndex == -1)
            tmp = dummyStateIndex = generatedStates;
        else
            tmp = ++dummyStateIndex;
    } else
        tmp = nameSet[toRet];
    stateNameToReturn = new Integer(tmp);
    stateNameForComposite.put(stateSetString, stateNameToReturn);
    compositeStateTable.put(stateSetString, nameSet);
    return tmp;
}
