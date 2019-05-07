private void addGlobalComponent(XSObject component, XSDDescription desc) {
    final String namespace = component.getNamespace();
    desc.setNamespace(namespace);
    final SchemaGrammar sg = getSchemaGrammar(desc);
    short componentType = component.getType();
    final String name = component.getName();
    switch(componentType) {
        case XSConstants.TYPE_DEFINITION:
            if (!((XSTypeDefinition) component).getAnonymous()) {
                if (sg.getGlobalTypeDecl(name) == null) {
                    sg.addGlobalTypeDecl((XSTypeDefinition) component);
                }
                // store the declaration in the extended map, using an empty location  
                if (sg.getGlobalTypeDecl(name, "") == null) {
                    sg.addGlobalTypeDecl((XSTypeDefinition) component, "");
                }
            }
            break;
        case XSConstants.ATTRIBUTE_DECLARATION:
            if (((XSAttributeDecl) component).getScope() == XSAttributeDecl.SCOPE_GLOBAL) {
                if (sg.getGlobalAttributeDecl(name) == null) {
                    sg.addGlobalAttributeDecl((XSAttributeDecl) component);
                }
                // store the declaration in the extended map, using an empty location  
                if (sg.getGlobalAttributeDecl(name, "") == null) {
                    sg.addGlobalAttributeDecl((XSAttributeDecl) component, "");
                }
            }
            break;
        case XSConstants.ATTRIBUTE_GROUP:
            if (sg.getGlobalAttributeDecl(name) == null) {
                sg.addGlobalAttributeGroupDecl((XSAttributeGroupDecl) component);
            }
            // store the declaration in the extended map, using an empty location  
            if (sg.getGlobalAttributeDecl(name, "") == null) {
                sg.addGlobalAttributeGroupDecl((XSAttributeGroupDecl) component, "");
            }
            break;
        case XSConstants.ELEMENT_DECLARATION:
            if (((XSElementDecl) component).getScope() == XSElementDecl.SCOPE_GLOBAL) {
                sg.addGlobalElementDeclAll((XSElementDecl) component);
                if (sg.getGlobalElementDecl(name) == null) {
                    sg.addGlobalElementDecl((XSElementDecl) component);
                }
                // store the declaration in the extended map, using an empty location  
                if (sg.getGlobalElementDecl(name, "") == null) {
                    sg.addGlobalElementDecl((XSElementDecl) component, "");
                }
            }
            break;
        case XSConstants.MODEL_GROUP_DEFINITION:
            if (sg.getGlobalGroupDecl(name) == null) {
                sg.addGlobalGroupDecl((XSGroupDecl) component);
            }
            // store the declaration in the extended map, using an empty location  
            if (sg.getGlobalGroupDecl(name, "") == null) {
                sg.addGlobalGroupDecl((XSGroupDecl) component, "");
            }
            break;
        case XSConstants.NOTATION_DECLARATION:
            if (sg.getGlobalNotationDecl(name) == null) {
                sg.addGlobalNotationDecl((XSNotationDecl) component);
            }
            // store the declaration in the extended map, using an empty location  
            if (sg.getGlobalNotationDecl(name, "") == null) {
                sg.addGlobalNotationDecl((XSNotationDecl) component, "");
            }
            break;
        case XSConstants.IDENTITY_CONSTRAINT:
        case XSConstants.ATTRIBUTE_USE:
        default:
            break;
    }
}
