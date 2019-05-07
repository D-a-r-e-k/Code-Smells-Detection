// getSchemaDocument(String, SAXInputSource, boolean, short, Element): Element  
/**
     * getSchemaDocument method uses DOMInputSource to parse a schema document.
     * @param schemaNamespace
     * @param schemaSource
     * @param mustResolve
     * @param referType
     * @param referElement
     * @return A schema Element.
     */
private Element getSchemaDocument(String schemaNamespace, DOMInputSource schemaSource, boolean mustResolve, short referType, Element referElement) {
    boolean hasInput = true;
    IOException exception = null;
    Element schemaElement = null;
    Element schemaRootElement = null;
    final Node node = schemaSource.getNode();
    short nodeType = -1;
    if (node != null) {
        nodeType = node.getNodeType();
        if (nodeType == Node.DOCUMENT_NODE) {
            schemaRootElement = DOMUtil.getRoot((Document) node);
        } else if (nodeType == Node.ELEMENT_NODE) {
            schemaRootElement = (Element) node;
        }
    }
    try {
        if (schemaRootElement != null) {
            // check whether the same document has been parsed before.   
            // If so, return the document corresponding to that system id.  
            XSDKey key = null;
            String schemaId = null;
            if (referType != XSDDescription.CONTEXT_PREPARSE) {
                schemaId = XMLEntityManager.expandSystemId(schemaSource.getSystemId(), schemaSource.getBaseSystemId(), false);
                boolean isDocument = (nodeType == Node.DOCUMENT_NODE);
                if (!isDocument) {
                    Node parent = schemaRootElement.getParentNode();
                    if (parent != null) {
                        isDocument = (parent.getNodeType() == Node.DOCUMENT_NODE);
                    }
                }
                if (isDocument) {
                    key = new XSDKey(schemaId, referType, schemaNamespace);
                    if ((schemaElement = (Element) fTraversed.get(key)) != null) {
                        fLastSchemaWasDuplicate = true;
                        return schemaElement;
                    }
                }
            }
            schemaElement = schemaRootElement;
            return getSchemaDocument0(key, schemaId, schemaElement);
        } else {
            hasInput = false;
        }
    } catch (IOException ioe) {
        exception = ioe;
    }
    return getSchemaDocument1(mustResolve, hasInput, schemaSource, referElement, exception);
}
