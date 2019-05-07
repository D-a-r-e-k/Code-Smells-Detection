// emptyElement(QName,XMLAttributes)  
/**
     * Character content.
     * 
     * @param text The content.
     *
     * @param augs   Additional information that may include infoset augmentations
     *
     * @throws XNIException Thrown by handler to signal an error.
     */
public void characters(XMLString text, Augmentations augs) throws XNIException {
    boolean callNextCharacters = true;
    // REVISIT: [Q] Is there a more efficient way of doing this?  
    //          Perhaps if the scanner told us so we don't have to  
    //          look at the characters again. -Ac  
    boolean allWhiteSpace = true;
    for (int i = text.offset; i < text.offset + text.length; i++) {
        if (!isSpace(text.ch[i])) {
            allWhiteSpace = false;
            break;
        }
    }
    // call the ignoreableWhiteSpace callback  
    // never call ignorableWhitespace if we are in cdata section  
    if (fInElementContent && allWhiteSpace && !fInCDATASection) {
        if (fDocumentHandler != null) {
            fDocumentHandler.ignorableWhitespace(text, augs);
            callNextCharacters = false;
        }
    }
    // validate  
    if (fPerformValidation) {
        if (fInElementContent) {
            if (fGrammarBucket.getStandalone() && fDTDGrammar.getElementDeclIsExternal(fCurrentElementIndex)) {
                if (allWhiteSpace) {
                    fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_WHITE_SPACE_IN_ELEMENT_CONTENT_WHEN_STANDALONE", null, XMLErrorReporter.SEVERITY_ERROR);
                }
            }
            if (!allWhiteSpace) {
                charDataInContent();
            }
            // For E15.2  
            if (augs != null && augs.getItem(Constants.CHAR_REF_PROBABLE_WS) == Boolean.TRUE) {
                fErrorReporter.reportError(XMLMessageFormatter.XML_DOMAIN, "MSG_CONTENT_INVALID_SPECIFIED", new Object[] { fCurrentElement.rawname, fDTDGrammar.getContentSpecAsString(fElementDepth), "character reference" }, XMLErrorReporter.SEVERITY_ERROR);
            }
        }
        if (fCurrentContentSpecType == XMLElementDecl.TYPE_EMPTY) {
            charDataInContent();
        }
    }
    // call handlers  
    if (callNextCharacters && fDocumentHandler != null) {
        fDocumentHandler.characters(text, augs);
    }
}
