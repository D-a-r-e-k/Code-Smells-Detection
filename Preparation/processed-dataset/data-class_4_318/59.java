// processRootTypeQName  
void processRootElementDeclQName(final javax.xml.namespace.QName rootElementDeclQName, final QName element) {
    String rootElementDeclNamespace = rootElementDeclQName.getNamespaceURI();
    if (rootElementDeclNamespace != null && rootElementDeclNamespace.equals(XMLConstants.NULL_NS_URI)) {
        rootElementDeclNamespace = null;
    }
    final SchemaGrammar grammarForRootElement = findSchemaGrammar(XSDDescription.CONTEXT_ELEMENT, rootElementDeclNamespace, null, null, null);
    if (grammarForRootElement != null) {
        fCurrentElemDecl = grammarForRootElement.getGlobalElementDecl(rootElementDeclQName.getLocalPart());
    }
    if (fCurrentElemDecl == null) {
        String declName = (rootElementDeclQName.getPrefix().equals(XMLConstants.DEFAULT_NS_PREFIX)) ? rootElementDeclQName.getLocalPart() : rootElementDeclQName.getPrefix() + ":" + rootElementDeclQName.getLocalPart();
        reportSchemaError("cvc-elt.1.a", new Object[] { declName });
    } else {
        checkElementMatchesRootElementDecl(fCurrentElemDecl, element);
    }
}
