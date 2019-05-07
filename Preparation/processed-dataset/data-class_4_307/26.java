// processingInstruction(String,XMLString)  
/**
     * This method notifies the start of a general entity.
     * <p>
     * <strong>Note:</strong> This method is not called for entity references
     * appearing as part of attribute values.
     * 
     * @param name     The name of the general entity.
     * @param identifier The resource identifier.
     * @param encoding The auto-detected IANA encoding name of the entity
     *                 stream. This value will be null in those situations
     *                 where the entity encoding is not auto-detected (e.g.
     *                 internal entities or a document entity that is
     *                 parsed from a java.io.Reader).
     * @param augs     Additional information that may include infoset augmentations
     *                 
     * @exception XNIException Thrown by handler to signal an error.
     */
public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
    if (fPerformValidation && fElementDepth >= 0 && fDTDGrammar != null) {
        fDTDGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
        // fixes E15.1  
        if (fTempElementDecl.type == XMLElementDecl.TYPE_EMPTY) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { fCurrentElement.rawname, "EMPTY", "ENTITY" }, XMLErrorReporter.SEVERITY_ERROR);
        }
        if (fGrammarBucket.getStandalone()) {
            XMLDTDLoader.checkStandaloneEntityRef(name, fDTDGrammar, fEntityDecl, fErrorReporter);
        }
    }
    if (fDocumentHandler != null) {
        fDocumentHandler.startGeneralEntity(name, identifier, encoding, augs);
    }
}
