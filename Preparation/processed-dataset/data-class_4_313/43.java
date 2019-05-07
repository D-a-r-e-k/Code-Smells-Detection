private XMLInputSource resolveSchemaSource(XSDDescription desc, boolean mustResolve, Element referElement, boolean usePairs) {
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
    return schemaSource;
}
