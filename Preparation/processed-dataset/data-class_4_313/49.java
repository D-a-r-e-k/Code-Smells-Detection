// getSchemaDocument0(XSDKey, String, Element): Element  
/**
     * Error handling code shared between the various getSchemaDocument() methods.
     */
private Element getSchemaDocument1(boolean mustResolve, boolean hasInput, XMLInputSource schemaSource, Element referElement, IOException ioe) {
    // either an error occured (exception), or empty input source was  
    // returned, we need to report an error or a warning  
    if (mustResolve) {
        if (hasInput) {
            reportSchemaError("schema_reference.4", new Object[] { schemaSource.getSystemId() }, referElement, ioe);
        } else {
            reportSchemaError("schema_reference.4", new Object[] { schemaSource == null ? "" : schemaSource.getSystemId() }, referElement, ioe);
        }
    } else if (hasInput) {
        reportSchemaWarning("schema_reference.4", new Object[] { schemaSource.getSystemId() }, referElement, ioe);
    }
    fLastSchemaWasDuplicate = false;
    return null;
}
