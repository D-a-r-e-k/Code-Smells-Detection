// endDocument()  
/**
     * A comment.
     * 
     * @param text The text in the comment.
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by application to signal an error.
     */
public void comment(XMLString text, Augmentations augs) throws XNIException {
    // fixes E15.1  
    if (fPerformValidation && fElementDepth >= 0 && fDTDGrammar != null) {
        fDTDGrammar.getElementDecl(fCurrentElementIndex, fTempElementDecl);
        if (fTempElementDecl.type == XMLElementDecl.TYPE_EMPTY) {
            fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { fCurrentElement.rawname, "EMPTY", "comment" }, XMLErrorReporter.SEVERITY_ERROR);
        }
    }
    // call handlers  
    if (fDocumentHandler != null) {
        fDocumentHandler.comment(text, augs);
    }
}
