private void expandRelatedAttributeComponents(XSAttributeDeclaration decl, Vector componentList, String namespace, Hashtable dependencies) {
    addRelatedType(decl.getTypeDefinition(), componentList, namespace, dependencies);
}
