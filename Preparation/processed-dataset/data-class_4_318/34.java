//  
// Protected methods  
//  
/** ensure element stack capacity */
void ensureStackCapacity() {
    if (fElementDepth == fElemDeclStack.length) {
        int newSize = fElementDepth + INC_STACK_SIZE;
        boolean[] newArrayB = new boolean[newSize];
        System.arraycopy(fSubElementStack, 0, newArrayB, 0, fElementDepth);
        fSubElementStack = newArrayB;
        XSElementDecl[] newArrayE = new XSElementDecl[newSize];
        System.arraycopy(fElemDeclStack, 0, newArrayE, 0, fElementDepth);
        fElemDeclStack = newArrayE;
        newArrayB = new boolean[newSize];
        System.arraycopy(fNilStack, 0, newArrayB, 0, fElementDepth);
        fNilStack = newArrayB;
        XSNotationDecl[] newArrayN = new XSNotationDecl[newSize];
        System.arraycopy(fNotationStack, 0, newArrayN, 0, fElementDepth);
        fNotationStack = newArrayN;
        XSTypeDefinition[] newArrayT = new XSTypeDefinition[newSize];
        System.arraycopy(fTypeStack, 0, newArrayT, 0, fElementDepth);
        fTypeStack = newArrayT;
        XSCMValidator[] newArrayC = new XSCMValidator[newSize];
        System.arraycopy(fCMStack, 0, newArrayC, 0, fElementDepth);
        fCMStack = newArrayC;
        newArrayB = new boolean[newSize];
        System.arraycopy(fSawTextStack, 0, newArrayB, 0, fElementDepth);
        fSawTextStack = newArrayB;
        newArrayB = new boolean[newSize];
        System.arraycopy(fStringContent, 0, newArrayB, 0, fElementDepth);
        fStringContent = newArrayB;
        newArrayB = new boolean[newSize];
        System.arraycopy(fStrictAssessStack, 0, newArrayB, 0, fElementDepth);
        fStrictAssessStack = newArrayB;
        int[][] newArrayIA = new int[newSize][];
        System.arraycopy(fCMStateStack, 0, newArrayIA, 0, fElementDepth);
        fCMStateStack = newArrayIA;
    }
}
