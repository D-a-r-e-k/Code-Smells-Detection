// reset(XMLComponentManager)  
/**
     * Traverse all the deferred local elements. This method should be called
     * by traverseSchemas after we've done with all the global declarations.
     */
void traverseLocalElements() {
    fElementTraverser.fDeferTraversingLocalElements = false;
    for (int i = 0; i < fLocalElemStackPos; i++) {
        Element currElem = fLocalElementDecl[i];
        //XSDocumentInfo currSchema = (XSDocumentInfo)fDoc2XSDocumentMap.get(DOMUtil.getDocument(currElem));  
        //XSDocumentInfo currSchema = (XSDocumentInfo)fDoc2XSDocumentMap.get(DOMUtil.getRoot(DOMUtil.getDocument(currElem)));  
        XSDocumentInfo currSchema = fLocalElementDecl_schema[i];
        SchemaGrammar currGrammar = fGrammarBucket.getGrammar(currSchema.fTargetNamespace);
        fElementTraverser.traverseLocal(fParticle[i], currElem, currSchema, currGrammar, fAllContext[i], fParent[i], fLocalElemNamespaceContext[i]);
        // If it's an empty particle, remove it from the containing component.  
        if (fParticle[i].fType == XSParticleDecl.PARTICLE_EMPTY) {
            XSModelGroupImpl group = null;
            if (fParent[i] instanceof XSComplexTypeDecl) {
                XSParticle p = ((XSComplexTypeDecl) fParent[i]).getParticle();
                if (p != null)
                    group = (XSModelGroupImpl) p.getTerm();
            } else {
                group = ((XSGroupDecl) fParent[i]).fModelGroup;
            }
            if (group != null)
                removeParticle(group, fParticle[i]);
        }
    }
}
