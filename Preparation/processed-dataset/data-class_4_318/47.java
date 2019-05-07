//findSchemaGrammar  
private boolean hasSchemaComponent(SchemaGrammar grammar, short contextType, QName triggeringComponent) {
    if (grammar != null && triggeringComponent != null) {
        String localName = triggeringComponent.localpart;
        if (localName != null && localName.length() > 0) {
            switch(contextType) {
                case XSDDescription.CONTEXT_ELEMENT:
                    return grammar.getElementDeclaration(localName) != null;
                case XSDDescription.CONTEXT_ATTRIBUTE:
                    return grammar.getAttributeDeclaration(localName) != null;
                case XSDDescription.CONTEXT_XSITYPE:
                    return grammar.getTypeDefinition(localName) != null;
            }
        }
    }
    return false;
}
