/**
     * check whether this content violates UPA constraint.
     *
     * @param subGroupHandler the substitution group handler
     * @return true if this content model contains other or list wildcard
     */
public boolean checkUniqueParticleAttribution(SubstitutionGroupHandler subGroupHandler) throws XMLSchemaException {
    // Unique Particle Attribution  
    // store the conflict results between any two elements in fElemMap  
    // 0: not compared; -1: no conflict; 1: conflict  
    // initialize the conflict table (all 0 initially)  
    byte conflictTable[][] = new byte[fElemMapSize][fElemMapSize];
    // for each state, check whether it has overlap transitions  
    for (int i = 0; i < fTransTable.length && fTransTable[i] != null; i++) {
        for (int j = 0; j < fElemMapSize; j++) {
            for (int k = j + 1; k < fElemMapSize; k++) {
                if (fTransTable[i][j] != -1 && fTransTable[i][k] != -1) {
                    if (conflictTable[j][k] == 0) {
                        if (XSConstraints.overlapUPA(fElemMap[j], fElemMap[k], subGroupHandler)) {
                            if (fCountingStates != null) {
                                Occurence o = fCountingStates[i];
                                // If "i" is a counting state and exactly one of the transitions  
                                // loops back to "i" then the two particles do not overlap if  
                                // minOccurs == maxOccurs.  
                                if (o != null && fTransTable[i][j] == i ^ fTransTable[i][k] == i && o.minOccurs == o.maxOccurs) {
                                    conflictTable[j][k] = (byte) -1;
                                    continue;
                                }
                            }
                            conflictTable[j][k] = (byte) 1;
                        } else {
                            conflictTable[j][k] = (byte) -1;
                        }
                    }
                }
            }
        }
    }
    // report all errors  
    for (int i = 0; i < fElemMapSize; i++) {
        for (int j = 0; j < fElemMapSize; j++) {
            if (conflictTable[i][j] == 1) {
                //errors.newError("cos-nonambig", new Object[]{fElemMap[i].toString(),  
                //                                             fElemMap[j].toString()});  
                // REVISIT: do we want to report all errors? or just one?  
                throw new XMLSchemaException("cos-nonambig", new Object[] { fElemMap[i].toString(), fElemMap[j].toString() });
            }
        }
    }
    // if there is a other or list wildcard, we need to check this CM  
    // again, if this grammar is cached.  
    for (int i = 0; i < fElemMapSize; i++) {
        if (fElemMapType[i] == XSParticleDecl.PARTICLE_WILDCARD) {
            XSWildcardDecl wildcard = (XSWildcardDecl) fElemMap[i];
            if (wildcard.fType == XSWildcardDecl.NSCONSTRAINT_LIST || wildcard.fType == XSWildcardDecl.NSCONSTRAINT_NOT) {
                return true;
            }
        }
    }
    return false;
}
