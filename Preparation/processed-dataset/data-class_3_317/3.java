// oneTransition(QName, int[], SubstitutionGroupHandler):  Object  
Object findMatchingDecl(QName curElem, SubstitutionGroupHandler subGroupHandler) {
    Object matchingDecl = null;
    for (int elemIndex = 0; elemIndex < fElemMapSize; elemIndex++) {
        int type = fElemMapType[elemIndex];
        if (type == XSParticleDecl.PARTICLE_ELEMENT) {
            matchingDecl = subGroupHandler.getMatchingElemDecl(curElem, (XSElementDecl) fElemMap[elemIndex]);
            if (matchingDecl != null) {
                return matchingDecl;
            }
        } else if (type == XSParticleDecl.PARTICLE_WILDCARD) {
            if (((XSWildcardDecl) fElemMap[elemIndex]).allowNamespace(curElem.uri))
                return fElemMap[elemIndex];
        }
    }
    return null;
}
