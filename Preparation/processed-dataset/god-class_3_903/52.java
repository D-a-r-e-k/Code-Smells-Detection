private static void DumpCompositeStatesAsciiMoves(java.io.PrintWriter ostr, String key, int byteNum, boolean[] dumped) {
    int i;
    int[] nameSet = (int[]) allNextStates.get(key);
    if (nameSet.length == 1 || dumped[StateNameForComposite(key)])
        return;
    NfaState toBePrinted = null;
    int neededStates = 0;
    NfaState tmp;
    NfaState stateForCase = null;
    String toPrint = "";
    boolean stateBlock = (stateBlockTable.get(key) != null);
    for (i = 0; i < nameSet.length; i++) {
        tmp = (NfaState) allStates.get(nameSet[i]);
        if (tmp.asciiMoves[byteNum] != 0L) {
            if (neededStates++ == 1)
                break;
            else
                toBePrinted = tmp;
        } else
            dumped[tmp.stateName] = true;
        if (tmp.stateForCase != null) {
            if (stateForCase != null)
                throw new Error("JavaCC Bug: Please send mail to sankar@cs.stanford.edu : ");
            stateForCase = tmp.stateForCase;
        }
    }
    if (stateForCase != null)
        toPrint = stateForCase.PrintNoBreak(ostr, byteNum, dumped);
    if (neededStates == 0) {
        if (stateForCase != null && toPrint.equals(""))
            ostr.println("                  break;");
        return;
    }
    if (neededStates == 1) {
        //if (byteNum == 1) 
        //System.out.println(toBePrinted.stateName + " is the only state for " 
        //+ key + " ; and key is : " + StateNameForComposite(key)); 
        if (!toPrint.equals(""))
            ostr.print(toPrint);
        ostr.println("               case " + StateNameForComposite(key) + ":");
        if (!dumped[toBePrinted.stateName] && !stateBlock && toBePrinted.inNextOf > 1)
            ostr.println("               case " + toBePrinted.stateName + ":");
        dumped[toBePrinted.stateName] = true;
        toBePrinted.DumpAsciiMove(ostr, byteNum, dumped);
        return;
    }
    List partition = PartitionStatesSetForAscii(nameSet, byteNum);
    if (!toPrint.equals(""))
        ostr.print(toPrint);
    int keyState = StateNameForComposite(key);
    ostr.println("               case " + keyState + ":");
    if (keyState < generatedStates)
        dumped[keyState] = true;
    for (i = 0; i < partition.size(); i++) {
        List subSet = (List) partition.get(i);
        for (int j = 0; j < subSet.size(); j++) {
            tmp = (NfaState) subSet.get(j);
            if (stateBlock)
                dumped[tmp.stateName] = true;
            tmp.DumpAsciiMoveForCompositeState(ostr, byteNum, j != 0);
        }
    }
    if (stateBlock)
        ostr.println("                  break;");
    else
        ostr.println("                  break;");
}
