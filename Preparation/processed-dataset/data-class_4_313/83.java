private void addRelatedAttribute(XSAttributeDeclaration decl, Vector componentList, String namespace, Hashtable dependencies) {
    if (decl.getScope() == XSConstants.SCOPE_GLOBAL) {
        if (!componentList.contains(decl)) {
            Vector importedNamespaces = findDependentNamespaces(namespace, dependencies);
            addNamespaceDependency(namespace, decl.getNamespace(), importedNamespaces);
            componentList.add(decl);
        }
    } else {
        expandRelatedAttributeComponents(decl, componentList, namespace, dependencies);
    }
}
