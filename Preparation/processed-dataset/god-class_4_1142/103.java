// the purpose of this method is to keep up-to-date structures  
// we'll need for the feferred traversal of local elements.  
void fillInLocalElemInfo(Element elmDecl, XSDocumentInfo schemaDoc, int allContextFlags, XSObject parent, XSParticleDecl particle) {
    // if the stack is full, increase the size  
    if (fParticle.length == fLocalElemStackPos) {
        // increase size  
        XSParticleDecl[] newStackP = new XSParticleDecl[fLocalElemStackPos + INC_STACK_SIZE];
        System.arraycopy(fParticle, 0, newStackP, 0, fLocalElemStackPos);
        fParticle = newStackP;
        Element[] newStackE = new Element[fLocalElemStackPos + INC_STACK_SIZE];
        System.arraycopy(fLocalElementDecl, 0, newStackE, 0, fLocalElemStackPos);
        fLocalElementDecl = newStackE;
        XSDocumentInfo[] newStackE_schema = new XSDocumentInfo[fLocalElemStackPos + INC_STACK_SIZE];
        System.arraycopy(fLocalElementDecl_schema, 0, newStackE_schema, 0, fLocalElemStackPos);
        fLocalElementDecl_schema = newStackE_schema;
        int[] newStackI = new int[fLocalElemStackPos + INC_STACK_SIZE];
        System.arraycopy(fAllContext, 0, newStackI, 0, fLocalElemStackPos);
        fAllContext = newStackI;
        XSObject[] newStackC = new XSObject[fLocalElemStackPos + INC_STACK_SIZE];
        System.arraycopy(fParent, 0, newStackC, 0, fLocalElemStackPos);
        fParent = newStackC;
        String[][] newStackN = new String[fLocalElemStackPos + INC_STACK_SIZE][];
        System.arraycopy(fLocalElemNamespaceContext, 0, newStackN, 0, fLocalElemStackPos);
        fLocalElemNamespaceContext = newStackN;
    }
    fParticle[fLocalElemStackPos] = particle;
    fLocalElementDecl[fLocalElemStackPos] = elmDecl;
    fLocalElementDecl_schema[fLocalElemStackPos] = schemaDoc;
    fAllContext[fLocalElemStackPos] = allContextFlags;
    fParent[fLocalElemStackPos] = parent;
    fLocalElemNamespaceContext[fLocalElemStackPos++] = schemaDoc.fNamespaceSupport.getEffectiveLocalContext();
}
