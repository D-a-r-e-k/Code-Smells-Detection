// getGrpOrAttrGrpRedefinedByRestriction(int, QName, XSDocumentInfo):  Object  
// Since ID constraints can occur in local elements, unless we  
// wish to completely traverse all our DOM trees looking for ID  
// constraints while we're building our global name registries,  
// which seems terribly inefficient, we need to resolve keyrefs  
// after all parsing is complete.  This we can simply do by running through  
// fIdentityConstraintRegistry and calling traverseKeyRef on all  
// of the KeyRef nodes.  This unfortunately removes this knowledge  
// from the elementTraverser class (which must ignore keyrefs),  
// but there seems to be no efficient way around this...  
protected void resolveKeyRefs() {
    for (int i = 0; i < fKeyrefStackPos; i++) {
        XSDocumentInfo keyrefSchemaDoc = fKeyrefsMapXSDocumentInfo[i];
        keyrefSchemaDoc.fNamespaceSupport.makeGlobal();
        keyrefSchemaDoc.fNamespaceSupport.setEffectiveContext(fKeyrefNamespaceContext[i]);
        SchemaGrammar keyrefGrammar = fGrammarBucket.getGrammar(keyrefSchemaDoc.fTargetNamespace);
        // need to set <keyref> to hidden before traversing it,  
        // because it has global scope  
        DOMUtil.setHidden(fKeyrefs[i], fHiddenNodes);
        fKeyrefTraverser.traverse(fKeyrefs[i], fKeyrefElems[i], keyrefSchemaDoc, keyrefGrammar);
    }
}
