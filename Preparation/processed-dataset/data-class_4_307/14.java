// startDocument(XMLLocator,String)  
/**
     * Notifies of the presence of an XMLDecl line in the document. If
     * present, this method will be called immediately following the
     * startDocument call.
     * 
     * @param version    The XML version.
     * @param encoding   The IANA encoding name of the document, or null if
     *                   not specified.
     * @param standalone The standalone value, or null if not specified.     
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
    // save standalone state  
    fGrammarBucket.setStandalone(standalone != null && standalone.equals("yes"));
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.xmlDecl(version, encoding, standalone, augs);
    }
}
