/** Post tree build initialization. */
private void postTreeBuildInit(CMNode nodeCur) throws RuntimeException {
    // Set the maximum states on this node  
    nodeCur.setMaxStates(fLeafCount);
    XSCMLeaf leaf = null;
    int pos = 0;
    // Recurse as required  
    if (nodeCur.type() == XSParticleDecl.PARTICLE_WILDCARD) {
        leaf = (XSCMLeaf) nodeCur;
        pos = leaf.getPosition();
        fLeafList[pos] = leaf;
        fLeafListType[pos] = XSParticleDecl.PARTICLE_WILDCARD;
    } else if ((nodeCur.type() == XSModelGroupImpl.MODELGROUP_CHOICE) || (nodeCur.type() == XSModelGroupImpl.MODELGROUP_SEQUENCE)) {
        postTreeBuildInit(((XSCMBinOp) nodeCur).getLeft());
        postTreeBuildInit(((XSCMBinOp) nodeCur).getRight());
    } else if (nodeCur.type() == XSParticleDecl.PARTICLE_ZERO_OR_MORE || nodeCur.type() == XSParticleDecl.PARTICLE_ONE_OR_MORE || nodeCur.type() == XSParticleDecl.PARTICLE_ZERO_OR_ONE) {
        postTreeBuildInit(((XSCMUniOp) nodeCur).getChild());
    } else if (nodeCur.type() == XSParticleDecl.PARTICLE_ELEMENT) {
        //  Put this node in the leaf list at the current index if its  
        //  a non-epsilon leaf.  
        leaf = (XSCMLeaf) nodeCur;
        pos = leaf.getPosition();
        fLeafList[pos] = leaf;
        fLeafListType[pos] = XSParticleDecl.PARTICLE_ELEMENT;
    } else {
        throw new RuntimeException("ImplementationMessages.VAL_NIICM");
    }
}
