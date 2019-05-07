private void expandRelatedTypeComponents(XSTypeDefinition type, Vector componentList, String namespace, Hashtable dependencies) {
    if (type instanceof XSComplexTypeDecl) {
        expandRelatedComplexTypeComponents((XSComplexTypeDecl) type, componentList, namespace, dependencies);
    } else if (type instanceof XSSimpleTypeDecl) {
        expandRelatedSimpleTypeComponents((XSSimpleTypeDefinition) type, componentList, namespace, dependencies);
    }
}
