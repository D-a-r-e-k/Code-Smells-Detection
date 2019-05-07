// before traversing a schema's parse tree, need to reset all traversers and  
// clear all registries  
void prepareForTraverse() {
    fUnparsedAttributeRegistry.clear();
    fUnparsedAttributeGroupRegistry.clear();
    fUnparsedElementRegistry.clear();
    fUnparsedGroupRegistry.clear();
    fUnparsedIdentityConstraintRegistry.clear();
    fUnparsedNotationRegistry.clear();
    fUnparsedTypeRegistry.clear();
    fUnparsedAttributeRegistrySub.clear();
    fUnparsedAttributeGroupRegistrySub.clear();
    fUnparsedElementRegistrySub.clear();
    fUnparsedGroupRegistrySub.clear();
    fUnparsedIdentityConstraintRegistrySub.clear();
    fUnparsedNotationRegistrySub.clear();
    fUnparsedTypeRegistrySub.clear();
    for (int i = 1; i <= TYPEDECL_TYPE; i++) {
        fUnparsedRegistriesExt[i].clear();
    }
    fXSDocumentInfoRegistry.clear();
    fDependencyMap.clear();
    fDoc2XSDocumentMap.clear();
    fRedefine2XSDMap.clear();
    fRedefine2NSSupport.clear();
    fAllTNSs.removeAllElements();
    fImportMap.clear();
    fRoot = null;
    // clear local element stack  
    for (int i = 0; i < fLocalElemStackPos; i++) {
        fParticle[i] = null;
        fLocalElementDecl[i] = null;
        fLocalElementDecl_schema[i] = null;
        fLocalElemNamespaceContext[i] = null;
    }
    fLocalElemStackPos = 0;
    // and do same for keyrefs.  
    for (int i = 0; i < fKeyrefStackPos; i++) {
        fKeyrefs[i] = null;
        fKeyrefElems[i] = null;
        fKeyrefNamespaceContext[i] = null;
        fKeyrefsMapXSDocumentInfo[i] = null;
    }
    fKeyrefStackPos = 0;
    // create traversers if necessary  
    if (fAttributeChecker == null) {
        createTraversers();
    }
    // reset traversers  
    Locale locale = fErrorReporter.getLocale();
    fAttributeChecker.reset(fSymbolTable);
    fAttributeGroupTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fAttributeTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fComplexTypeTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fElementTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fGroupTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fKeyrefTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fNotationTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fSimpleTypeTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fUniqueOrKeyTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fWildCardTraverser.reset(fSymbolTable, fValidateAnnotations, locale);
    fRedefinedRestrictedAttributeGroupRegistry.clear();
    fRedefinedRestrictedGroupRegistry.clear();
    fGlobalAttrDecls.clear();
    fGlobalAttrGrpDecls.clear();
    fGlobalElemDecls.clear();
    fGlobalGroupDecls.clear();
    fGlobalNotationDecls.clear();
    fGlobalIDConstraintDecls.clear();
    fGlobalTypeDecls.clear();
}
