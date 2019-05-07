private boolean canAddComponent(XSObject component, XSDDescription desc) {
    desc.setNamespace(component.getNamespace());
    final SchemaGrammar sg = findGrammar(desc, false);
    if (sg == null) {
        return true;
    } else if (sg.isImmutable()) {
        return false;
    }
    short componentType = component.getType();
    final String name = component.getName();
    switch(componentType) {
        case XSConstants.TYPE_DEFINITION:
            if (sg.getGlobalTypeDecl(name) == component) {
                return true;
            }
            break;
        case XSConstants.ATTRIBUTE_DECLARATION:
            if (sg.getGlobalAttributeDecl(name) == component) {
                return true;
            }
            break;
        case XSConstants.ATTRIBUTE_GROUP:
            if (sg.getGlobalAttributeDecl(name) == component) {
                return true;
            }
            break;
        case XSConstants.ELEMENT_DECLARATION:
            if (sg.getGlobalElementDecl(name) == component) {
                return true;
            }
            break;
        case XSConstants.MODEL_GROUP_DEFINITION:
            if (sg.getGlobalGroupDecl(name) == component) {
                return true;
            }
            break;
        case XSConstants.NOTATION_DECLARATION:
            if (sg.getGlobalNotationDecl(name) == component) {
                return true;
            }
            break;
        case XSConstants.IDENTITY_CONSTRAINT:
        case XSConstants.ATTRIBUTE_USE:
        default:
            return true;
    }
    return false;
}
