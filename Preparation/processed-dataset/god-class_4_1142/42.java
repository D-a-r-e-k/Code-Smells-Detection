// getSchema(String, String, String, boolean, short):  Document  
private Element resolveSchema(XMLInputSource schemaSource, XSDDescription desc, boolean mustResolve, Element referElement) {
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
