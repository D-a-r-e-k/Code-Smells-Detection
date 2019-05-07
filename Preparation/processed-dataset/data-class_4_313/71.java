private void expandRelatedElementComponents(XSElementDeclaration decl, Vector componentList, String namespace, Hashtable dependencies) {
    addRelatedType(decl.getTypeDefinition(), componentList, namespace, dependencies);
    /*final XSTypeDefinition enclosingType = decl.getEnclosingCTDefinition();
        if (enclosingType != null) {
            addRelatedType(enclosingType, componentList, namespace, dependencies);
        }*/
    final XSElementDeclaration subElemDecl = decl.getSubstitutionGroupAffiliation();
    if (subElemDecl != null) {
        addRelatedElement(subElemDecl, componentList, namespace, dependencies);
    }
}
