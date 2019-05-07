private void expandRelatedAttributeUseComponents(XSAttributeUse component, Vector componentList, String namespace, Hashtable dependencies) {
    addRelatedAttribute(component.getAttrDeclaration(), componentList, namespace, dependencies);
}
