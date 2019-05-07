/**
     * Calculates the follow list of the current node.
     *
     * @param nodeCur The curent node.
     *
     * @exception RuntimeException Thrown if follow list cannot be calculated.
     */
private void calcFollowList(CMNode nodeCur) {
    // Recurse as required  
    if (nodeCur.type() == XSModelGroupImpl.MODELGROUP_CHOICE) {
        // Recurse only  
        calcFollowList(((XSCMBinOp) nodeCur).getLeft());
        calcFollowList(((XSCMBinOp) nodeCur).getRight());
    } else if (nodeCur.type() == XSModelGroupImpl.MODELGROUP_SEQUENCE) {
        // Recurse first  
        calcFollowList(((XSCMBinOp) nodeCur).getLeft());
        calcFollowList(((XSCMBinOp) nodeCur).getRight());
        //  
        //  Now handle our level. We use our left child's last pos  
        //  set and our right child's first pos set, so go ahead and  
        //  get them ahead of time.  
        //  
        final CMStateSet last = ((XSCMBinOp) nodeCur).getLeft().lastPos();
        final CMStateSet first = ((XSCMBinOp) nodeCur).getRight().firstPos();
        //  
        //  Now, for every position which is in our left child's last set  
        //  add all of the states in our right child's first set to the  
        //  follow set for that position.  
        //  
        for (int index = 0; index < fLeafCount; index++) {
            if (last.getBit(index))
                fFollowList[index].union(first);
        }
    } else if (nodeCur.type() == XSParticleDecl.PARTICLE_ZERO_OR_MORE || nodeCur.type() == XSParticleDecl.PARTICLE_ONE_OR_MORE) {
        // Recurse first  
        calcFollowList(((XSCMUniOp) nodeCur).getChild());
        //  
        //  Now handle our level. We use our own first and last position  
        //  sets, so get them up front.  
        //  
        final CMStateSet first = nodeCur.firstPos();
        final CMStateSet last = nodeCur.lastPos();
        //  
        //  For every position which is in our last position set, add all  
        //  of our first position states to the follow set for that  
        //  position.  
        //  
        for (int index = 0; index < fLeafCount; index++) {
            if (last.getBit(index))
                fFollowList[index].union(first);
        }
    } else if (nodeCur.type() == XSParticleDecl.PARTICLE_ZERO_OR_ONE) {
        // Recurse only  
        calcFollowList(((XSCMUniOp) nodeCur).getChild());
    }
}
