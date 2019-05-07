protected Object traverseGlobalDecl(int declType, Element decl, XSDocumentInfo schemaDoc, SchemaGrammar grammar) {
    Object retObj = null;
    DOMUtil.setHidden(decl, fHiddenNodes);
    SchemaNamespaceSupport nsSupport = null;
    // if the parent is <redefine> use the namespace delcs for it.  
    Element parent = DOMUtil.getParent(decl);
    if (DOMUtil.getLocalName(parent).equals(SchemaSymbols.ELT_REDEFINE))
        nsSupport = (SchemaNamespaceSupport) fRedefine2NSSupport.get(parent);
    // back up the current SchemaNamespaceSupport, because we need to provide  
    // a fresh one to the traverseGlobal methods.  
    schemaDoc.backupNSSupport(nsSupport);
    // traverse the referenced global component  
    switch(declType) {
        case TYPEDECL_TYPE:
            if (DOMUtil.getLocalName(decl).equals(SchemaSymbols.ELT_COMPLEXTYPE)) {
                retObj = fComplexTypeTraverser.traverseGlobal(decl, schemaDoc, grammar);
            } else {
                retObj = fSimpleTypeTraverser.traverseGlobal(decl, schemaDoc, grammar);
            }
            break;
        case ATTRIBUTE_TYPE:
            retObj = fAttributeTraverser.traverseGlobal(decl, schemaDoc, grammar);
            break;
        case ELEMENT_TYPE:
            retObj = fElementTraverser.traverseGlobal(decl, schemaDoc, grammar);
            break;
        case ATTRIBUTEGROUP_TYPE:
            retObj = fAttributeGroupTraverser.traverseGlobal(decl, schemaDoc, grammar);
            break;
        case GROUP_TYPE:
            retObj = fGroupTraverser.traverseGlobal(decl, schemaDoc, grammar);
            break;
        case NOTATION_TYPE:
            retObj = fNotationTraverser.traverse(decl, schemaDoc, grammar);
            break;
        case IDENTITYCONSTRAINT_TYPE:
            // identity constraints should have been parsed already...  
            // we should never get here  
            break;
    }
    // restore the previous SchemaNamespaceSupport, so that the caller can get  
    // proper namespace binding.  
    schemaDoc.restoreNSSupport();
    return retObj;
}
