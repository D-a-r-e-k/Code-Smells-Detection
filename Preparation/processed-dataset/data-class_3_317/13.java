/**
     * Check which elements are valid to appear at this point. This method also
     * works if the state is in error, in which case it returns what should
     * have been seen.
     * 
     * @param state  the current state
     * @return       a Vector whose entries are instances of
     *               either XSWildcardDecl or XSElementDecl.
     */
public Vector whatCanGoHere(int[] state) {
    int curState = state[0];
    if (curState < 0)
        curState = state[1];
    Occurence o = (fCountingStates != null) ? fCountingStates[curState] : null;
    int count = state[2];
    Vector ret = new Vector();
    for (int elemIndex = 0; elemIndex < fElemMapSize; elemIndex++) {
        int nextState = fTransTable[curState][elemIndex];
        if (nextState != -1) {
            if (o != null) {
                if (curState == nextState) {
                    // Do not include transitions which loop back to the  
                    // current state if we've looped the maximum number  
                    // of times or greater.  
                    if (count >= o.maxOccurs && o.maxOccurs != SchemaSymbols.OCCURRENCE_UNBOUNDED) {
                        continue;
                    }
                } else if (count < o.minOccurs) {
                    continue;
                }
            }
            ret.addElement(fElemMap[elemIndex]);
        }
    }
    return ret;
}
