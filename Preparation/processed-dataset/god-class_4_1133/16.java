// scanXMLDeclOrTextDecl(boolean)  
/**
     * Scans a processing data. This is needed to handle the situation
     * where a document starts with a processing instruction whose 
     * target name <em>starts with</em> "xml". (e.g. xmlfoo)
     *
     * @param target The PI target
     * @param data The string to fill in with the data
     */
protected void scanPIData(String target, XMLString data) throws IOException, XNIException {
    super.scanPIData(target, data);
    fMarkupDepth--;
    // call handler  
    if (fDocumentHandler != null) {
        fDocumentHandler.processingInstruction(target, data, null);
    }
}
