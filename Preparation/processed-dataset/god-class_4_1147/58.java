// elementLocallyValidComplexType  
void processRootTypeQName(final javax.xml.namespace.QName rootTypeQName) {
    String rootTypeNamespace = rootTypeQName.getNamespaceURI();
    if (rootTypeNamespace != null && rootTypeNamespace.equals(XMLConstants.NULL_NS_URI)) {
        rootTypeNamespace = null;
    }
    if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(rootTypeNamespace)) {
        fCurrentType = SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl(rootTypeQName.getLocalPart());
    } else {
        final SchemaGrammar grammarForRootType = findSchemaGrammar(XSDDescription.CONTEXT_ELEMENT, rootTypeNamespace, null, null, null);
        if (grammarForRootType != null) {
            fCurrentType = grammarForRootType.getGlobalTypeDecl(rootTypeQName.getLocalPart());
        }
    }
    if (fCurrentType == null) {
        String typeName = (rootTypeQName.getPrefix().equals(XMLConstants.DEFAULT_NS_PREFIX)) ? rootTypeQName.getLocalPart() : rootTypeQName.getPrefix() + ":" + rootTypeQName.getLocalPart();
        reportSchemaError("cvc-type.1", new Object[] { typeName });
    }
}
