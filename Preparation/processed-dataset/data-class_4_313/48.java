// getSchemaDocument(String, StAXInputSource, boolean, short, Element): Element  
/**
     * Code shared between the various getSchemaDocument() methods which
     * stores mapping information for the document.
     */
private Element getSchemaDocument0(XSDKey key, String schemaId, Element schemaElement) {
    // now we need to store the mapping information from system id  
    // to the document. also from the document to the system id.  
    if (key != null) {
        fTraversed.put(key, schemaElement);
    }
    if (schemaId != null) {
        fDoc2SystemId.put(schemaElement, schemaId);
    }
    fLastSchemaWasDuplicate = false;
    return schemaElement;
}
