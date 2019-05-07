private void expandRelatedSimpleTypeComponents(XSSimpleTypeDefinition type, Vector componentList, String namespace, Hashtable dependencies) {
    final XSTypeDefinition baseType = type.getBaseType();
    if (baseType != null) {
        addRelatedType(baseType, componentList, namespace, dependencies);
    }
    final XSTypeDefinition itemType = type.getItemType();
    if (itemType != null) {
        addRelatedType(itemType, componentList, namespace, dependencies);
    }
    final XSTypeDefinition primitiveType = type.getPrimitiveType();
    if (primitiveType != null) {
        addRelatedType(primitiveType, componentList, namespace, dependencies);
    }
    final XSObjectList memberTypes = type.getMemberTypes();
    if (memberTypes.size() > 0) {
        for (int i = 0; i < memberTypes.size(); i++) {
            addRelatedType((XSTypeDefinition) memberTypes.item(i), componentList, namespace, dependencies);
        }
    }
}
