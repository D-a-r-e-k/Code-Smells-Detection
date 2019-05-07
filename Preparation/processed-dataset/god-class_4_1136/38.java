// checkContent(int,int,QName[]):int  
/** Returns the content spec type for an element index. */
private int getContentSpecType(int elementIndex) {
    int contentSpecType = -1;
    if (elementIndex > -1) {
        if (fDTDGrammar.getElementDecl(elementIndex, fTempElementDecl)) {
            contentSpecType = fTempElementDecl.type;
        }
    }
    return contentSpecType;
}
