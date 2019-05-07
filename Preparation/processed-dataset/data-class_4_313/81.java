private void addRelatedType(XSTypeDefinition type, Vector componentList, String namespace, Hashtable dependencies) {
    if (!type.getAnonymous()) {
        if (!type.getNamespace().equals(SchemaSymbols.URI_SCHEMAFORSCHEMA)) {
            //REVISIT - do we use == instead  
            if (!componentList.contains(type)) {
                final Vector importedNamespaces = findDependentNamespaces(namespace, dependencies);
                addNamespaceDependency(namespace, type.getNamespace(), importedNamespaces);
                componentList.add(type);
            }
        }
    } else {
        expandRelatedTypeComponents(type, componentList, namespace, dependencies);
    }
}
