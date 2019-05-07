// storeKeyref (Element, XSDocumentInfo, XSElementDecl): void  
/**
     * resolveSchema method is responsible for resolving location of the schema (using XMLEntityResolver),
     * and if it was succefully resolved getting the schema Document.
     * @param desc
     * @param mustResolve
     * @param referElement
     * @return A schema Element or null.
     */
private Element resolveSchema(XSDDescription desc, boolean mustResolve, Element referElement, boolean usePairs) {
    XMLInputSource schemaSource = null;
    try {
        Hashtable pairs = usePairs ? fLocationPairs : EMPTY_TABLE;
        schemaSource = XMLSchemaLoader.resolveDocument(desc, pairs, fEntityResolver);
    } catch (IOException ex) {
        if (mustResolve) {
            reportSchemaError("schema_reference.4", new Object[] { desc.getLocationHints()[0] }, referElement);
        } else {
            reportSchemaWarning("schema_reference.4", new Object[] { desc.getLocationHints()[0] }, referElement);
        }
    }
    if (schemaSource instanceof DOMInputSource) {
        return getSchemaDocument(desc.getTargetNamespace(), (DOMInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
    } else if (schemaSource instanceof SAXInputSource) {
        return getSchemaDocument(desc.getTargetNamespace(), (SAXInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
    } else if (schemaSource instanceof StAXInputSource) {
        return getSchemaDocument(desc.getTargetNamespace(), (StAXInputSource) schemaSource, mustResolve, desc.getContextType(), referElement);
    } else if (schemaSource instanceof XSInputSource) {
        return getSchemaDocument((XSInputSource) schemaSource, desc);
    }
    // XSInputSource  
    return getSchemaDocument(desc.getTargetNamespace(), schemaSource, mustResolve, desc.getContextType(), referElement);
}
