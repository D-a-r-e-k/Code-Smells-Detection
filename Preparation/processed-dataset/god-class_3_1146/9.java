/**
     * Dumps the tree of the current node to standard output.
     *
     * @param nodeCur The current node.
     * @param level   The maximum levels to output.
     *
     * @exception RuntimeException Thrown on error.
     */
private void dumpTree(CMNode nodeCur, int level) {
    for (int index = 0; index < level; index++) System.out.print("   ");
    int type = nodeCur.type();
    switch(type) {
        case XSModelGroupImpl.MODELGROUP_CHOICE:
        case XSModelGroupImpl.MODELGROUP_SEQUENCE:
            {
                if (type == XSModelGroupImpl.MODELGROUP_CHOICE)
                    System.out.print("Choice Node ");
                else
                    System.out.print("Seq Node ");
                if (nodeCur.isNullable())
                    System.out.print("Nullable ");
                System.out.print("firstPos=");
                System.out.print(nodeCur.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(nodeCur.lastPos().toString());
                dumpTree(((XSCMBinOp) nodeCur).getLeft(), level + 1);
                dumpTree(((XSCMBinOp) nodeCur).getRight(), level + 1);
                break;
            }
        case XSParticleDecl.PARTICLE_ZERO_OR_MORE:
        case XSParticleDecl.PARTICLE_ONE_OR_MORE:
        case XSParticleDecl.PARTICLE_ZERO_OR_ONE:
            {
                System.out.print("Rep Node ");
                if (nodeCur.isNullable())
                    System.out.print("Nullable ");
                System.out.print("firstPos=");
                System.out.print(nodeCur.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(nodeCur.lastPos().toString());
                dumpTree(((XSCMUniOp) nodeCur).getChild(), level + 1);
                break;
            }
        case XSParticleDecl.PARTICLE_ELEMENT:
            {
                System.out.print("Leaf: (pos=" + ((XSCMLeaf) nodeCur).getPosition() + "), " + "(elemIndex=" + ((XSCMLeaf) nodeCur).getLeaf() + ") ");
                if (nodeCur.isNullable())
                    System.out.print(" Nullable ");
                System.out.print("firstPos=");
                System.out.print(nodeCur.firstPos().toString());
                System.out.print(" lastPos=");
                System.out.println(nodeCur.lastPos().toString());
                break;
            }
        case XSParticleDecl.PARTICLE_WILDCARD:
            System.out.print("Any Node: ");
            System.out.print("firstPos=");
            System.out.print(nodeCur.firstPos().toString());
            System.out.print(" lastPos=");
            System.out.println(nodeCur.lastPos().toString());
            break;
        default:
            {
                throw new RuntimeException("ImplementationMessages.VAL_NIICM");
            }
    }
}
