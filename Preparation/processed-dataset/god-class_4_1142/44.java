/**
     * getSchemaDocument method uses XMLInputSource to parse a schema document.
     * @param schemaNamespace
     * @param schemaSource
     * @param mustResolve
     * @param referType
     * @param referElement
     * @return A schema Element.
     */
private Element getSchemaDocument(String schemaNamespace, XMLInputSource schemaSource, boolean mustResolve, short referType, Element referElement) {
    boolean hasInput = true;
    IOException exception = null;
    // contents of this method will depend on the system we adopt for entity resolution--i.e., XMLEntityHandler, EntityHandler, etc.  
    Element schemaElement = null;
    try {
        // when the system id and byte stream and character stream  
        // of the input source are all null, it's  
        // impossible to find the schema document. so we skip in  
        // this case. otherwise we'll receive some NPE or  
        // file not found errors. but schemaHint=="" is perfectly  
        // legal for import.  
        if (schemaSource != null && (schemaSource.getSystemId() != null || schemaSource.getByteStream() != null || schemaSource.getCharacterStream() != null)) {
            // When the system id of the input source is used, first try to  
            // expand it, and check whether the same document has been  
            // parsed before. If so, return the document corresponding to  
            // that system id.  
            XSDKey key = null;
            String schemaId = null;
            if (referType != XSDDescription.CONTEXT_PREPARSE) {
                schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                key = new XSDKey(schemaId, referType, schemaNamespace);
                if ((schemaElement = (Element) fTraversed.get(key)) != null) {
                    fLastSchemaWasDuplicate = true;
                    return schemaElement;
                }
            }
            fSchemaParser.parse(schemaSource);
            Document schemaDocument = fSchemaParser.getDocument();
            schemaElement = schemaDocument != null ? DOMUtil.getRoot(schemaDocument) : null;
            return getSchemaDocument0(key, schemaId, schemaElement);
        } else {
            hasInput = false;
        }
    } catch (IOException ex) {
        exception = ex;
    }
    return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
}
