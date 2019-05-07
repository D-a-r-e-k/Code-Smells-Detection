String GetEpsilonMovesString() {
    int[] stateNames = new int[usefulEpsilonMoves];
    int cnt = 0;
    if (epsilonMovesString != null)
        return epsilonMovesString;
    if (usefulEpsilonMoves > 0) {
        NfaState tempState;
        epsilonMovesString = "{ ";
        for (int i = 0; i < epsilonMoves.size(); i++) {
            if ((tempState = (NfaState) epsilonMoves.get(i)).HasTransitions()) {
                if (tempState.stateName == -1)
                    tempState.GenerateCode();
                ((NfaState) indexedAllStates.get(tempState.stateName)).inNextOf++;
                stateNames[cnt] = tempState.stateName;
                epsilonMovesString += tempState.stateName + ", ";
                if (cnt++ > 0 && cnt % 16 == 0)
                    epsilonMovesString += "\n";
            }
        }
        epsilonMovesString += "};";
    }
    usefulEpsilonMoves = cnt;
    if (epsilonMovesString != null && allNextStates.get(epsilonMovesString) == null) {
        int[] statesToPut = new int[usefulEpsilonMoves];
        System.arraycopy(stateNames, 0, statesToPut, 0, cnt);
        allNextStates.put(epsilonMovesString, statesToPut);
    }
    return epsilonMovesString;
}
