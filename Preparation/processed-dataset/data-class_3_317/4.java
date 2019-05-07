// findMatchingDecl(QName, SubstitutionGroupHandler): Object  
Object findMatchingDecl(QName curElem, int[] state, SubstitutionGroupHandler subGroupHandler, int elemIndex) {
    int curState = state[0];
    int nextState = 0;
    Object matchingDecl = null;
    while (++elemIndex < fElemMapSize) {
        nextState = fTransTable[curState][elemIndex];
        if (nextState == -1)
            continue;
        int type = fElemMapType[elemIndex];
        if (type == XSParticleDecl.PARTICLE_ELEMENT) {
            matchingDecl = subGroupHandler.getMatchingElemDecl(curElem, (XSElementDecl) fElemMap[elemIndex]);
            if (matchingDecl != null) {
                break;
            }
        } else if (type == XSParticleDecl.PARTICLE_WILDCARD) {
            if (((XSWildcardDecl) fElemMap[elemIndex]).allowNamespace(curElem.uri)) {
                matchingDecl = fElemMap[elemIndex];
                break;
            }
        }
    }
    // if we still can't find a match, set the state to FIRST_ERROR and return null  
    if (elemIndex == fElemMapSize) {
        state[1] = state[0];
        state[0] = XSCMValidator.FIRST_ERROR;
        return findMatchingDecl(curElem, subGroupHandler);
    }
    // if we found a match, set the next state and reset the   
    // counter if the next state is a counting state.  
    state[0] = nextState;
    final Occurence o = fCountingStates[nextState];
    if (o != null) {
        state[2] = (elemIndex == o.elemIndex) ? 1 : 0;
    }
    return matchingDecl;
}
