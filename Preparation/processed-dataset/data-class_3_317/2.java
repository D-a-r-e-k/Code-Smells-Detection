/**
     * one transition only
     *
     * @param curElem The current element's QName
     * @param state stack to store the previous state
     * @param subGroupHandler the substitution group handler
     *
     * @return  null if transition is invalid; otherwise the Object corresponding to the
     *      XSElementDecl or XSWildcardDecl identified.  Also, the
     *      state array will be modified to include the new state; this so that the validator can
     *      store it away.
     *
     * @exception RuntimeException thrown on error
     */
public Object oneTransition(QName curElem, int[] state, SubstitutionGroupHandler subGroupHandler) {
    int curState = state[0];
    if (curState == XSCMValidator.FIRST_ERROR || curState == XSCMValidator.SUBSEQUENT_ERROR) {
        // there was an error last time; so just go find correct Object in fElemmMap.  
        // ... after resetting state[0].  
        if (curState == XSCMValidator.FIRST_ERROR)
            state[0] = XSCMValidator.SUBSEQUENT_ERROR;
        return findMatchingDecl(curElem, subGroupHandler);
    }
    int nextState = 0;
    int elemIndex = 0;
    Object matchingDecl = null;
    for (; elemIndex < fElemMapSize; elemIndex++) {
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
    // if we still can't find a match, set the state to first_error  
    // and return null  
    if (elemIndex == fElemMapSize) {
        state[1] = state[0];
        state[0] = XSCMValidator.FIRST_ERROR;
        return findMatchingDecl(curElem, subGroupHandler);
    }
    if (fCountingStates != null) {
        Occurence o = fCountingStates[curState];
        if (o != null) {
            if (curState == nextState) {
                if (++state[2] > o.maxOccurs && o.maxOccurs != SchemaSymbols.OCCURRENCE_UNBOUNDED) {
                    // It's likely that we looped too many times on the current state  
                    // however it's possible that we actually matched another particle  
                    // which allows the same name.  
                    //  
                    // Consider:  
                    //  
                    // <xs:sequence>  
                    //  <xs:element name="foo" type="xs:string" minOccurs="3" maxOccurs="3"/>  
                    //  <xs:element name="foo" type="xs:string" fixed="bar"/>  
                    // </xs:sequence>  
                    //  
                    // and  
                    //  
                    // <xs:sequence>  
                    //  <xs:element name="foo" type="xs:string" minOccurs="3" maxOccurs="3"/>  
                    //  <xs:any namespace="##any" processContents="skip"/>  
                    // </xs:sequence>  
                    //  
                    // In the DFA there will be two transitions from the current state which   
                    // allow "foo". Note that this is not a UPA violation. The ambiguity of which  
                    // transition to take is resolved by the current value of the counter. Since   
                    // we've already seen enough instances of the first "foo" perhaps there is  
                    // another element declaration or wildcard deeper in the element map which  
                    // matches.  
                    return findMatchingDecl(curElem, state, subGroupHandler, elemIndex);
                }
            } else if (state[2] < o.minOccurs) {
                // not enough loops on the current state.  
                state[1] = state[0];
                state[0] = XSCMValidator.FIRST_ERROR;
                return findMatchingDecl(curElem, subGroupHandler);
            } else {
                // Exiting a counting state. If we're entering a new  
                // counting state, reset the counter.  
                o = fCountingStates[nextState];
                if (o != null) {
                    state[2] = (elemIndex == o.elemIndex) ? 1 : 0;
                }
            }
        } else {
            o = fCountingStates[nextState];
            if (o != null) {
                // Entering a new counting state. Reset the counter.  
                // If we've already seen one instance of the looping  
                // particle set the counter to 1, otherwise set it   
                // to 0.  
                state[2] = (elemIndex == o.elemIndex) ? 1 : 0;
            }
        }
    }
    state[0] = nextState;
    return matchingDecl;
}
