private void expandRelatedComponents(XSObject component, Vector componentList, Hashtable dependencies) {
    short componentType = component.getType();
    switch(componentType) {
        case XSConstants.TYPE_DEFINITION:
            expandRelatedTypeComponents((XSTypeDefinition) component, componentList, component.getNamespace(), dependencies);
            break;
        case XSConstants.ATTRIBUTE_DECLARATION:
            expandRelatedAttributeComponents((XSAttributeDeclaration) component, componentList, component.getNamespace(), dependencies);
            break;
        case XSConstants.ATTRIBUTE_GROUP:
            expandRelatedAttributeGroupComponents((XSAttributeGroupDefinition) component, componentList, component.getNamespace(), dependencies);
        case XSConstants.ELEMENT_DECLARATION:
            expandRelatedElementComponents((XSElementDeclaration) component, componentList, component.getNamespace(), dependencies);
            break;
        case XSConstants.MODEL_GROUP_DEFINITION:
            expandRelatedModelGroupDefinitionComponents((XSModelGroupDefinition) component, componentList, component.getNamespace(), dependencies);
        case XSConstants.ATTRIBUTE_USE:
        //expandRelatedAttributeUseComponents((XSAttributeUse)component, componentList, dependencies);  
        case XSConstants.NOTATION_DECLARATION:
        case XSConstants.IDENTITY_CONSTRAINT:
        default:
            break;
    }
}
