// This method squirrels away <keyref> declarations--along with the element  
// decls and namespace bindings they might find handy.  
protected void storeKeyRef(Element keyrefToStore, XSDocumentInfo schemaDoc, XSElementDecl currElemDecl) {
    String keyrefName = DOMUtil.getAttrValue(keyrefToStore, SchemaSymbols.ATT_NAME);
    if (keyrefName.length() != 0) {
        String keyrefQName = schemaDoc.fTargetNamespace == null ? "," + keyrefName : schemaDoc.fTargetNamespace + "," + keyrefName;
        checkForDuplicateNames(keyrefQName, IDENTITYCONSTRAINT_TYPE, fUnparsedIdentityConstraintRegistry, fUnparsedIdentityConstraintRegistrySub, keyrefToStore, schemaDoc);
    }
    // now set up all the registries we'll need...  
    // check array sizes  
    if (fKeyrefStackPos == fKeyrefs.length) {
        Element[] elemArray = new Element[fKeyrefStackPos + INC_KEYREF_STACK_AMOUNT];
        System.arraycopy(fKeyrefs, 0, elemArray, 0, fKeyrefStackPos);
        fKeyrefs = elemArray;
        XSElementDecl[] declArray = new XSElementDecl[fKeyrefStackPos + INC_KEYREF_STACK_AMOUNT];
        System.arraycopy(fKeyrefElems, 0, declArray, 0, fKeyrefStackPos);
        fKeyrefElems = declArray;
        String[][] stringArray = new String[fKeyrefStackPos + INC_KEYREF_STACK_AMOUNT][];
        System.arraycopy(fKeyrefNamespaceContext, 0, stringArray, 0, fKeyrefStackPos);
        fKeyrefNamespaceContext = stringArray;
        XSDocumentInfo[] xsDocumentInfo = new XSDocumentInfo[fKeyrefStackPos + INC_KEYREF_STACK_AMOUNT];
        System.arraycopy(fKeyrefsMapXSDocumentInfo, 0, xsDocumentInfo, 0, fKeyrefStackPos);
        fKeyrefsMapXSDocumentInfo = xsDocumentInfo;
    }
    fKeyrefs[fKeyrefStackPos] = keyrefToStore;
    fKeyrefElems[fKeyrefStackPos] = currElemDecl;
    fKeyrefNamespaceContext[fKeyrefStackPos] = schemaDoc.fNamespaceSupport.getEffectiveLocalContext();
    fKeyrefsMapXSDocumentInfo[fKeyrefStackPos++] = schemaDoc;
}
