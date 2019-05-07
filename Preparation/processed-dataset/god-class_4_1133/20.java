// scanStartElementName()  
/**
     * Scans the remainder of a start or empty tag after the element name.
     * 
     * @see #scanStartElement
     * @return True if element is empty.
     */
protected boolean scanStartElementAfterName() throws IOException, XNIException {
    String rawname = fElementQName.rawname;
    // push element stack  
    fCurrentElement = fElementStack.pushElement(fElementQName);
    // attributes  
    boolean empty = false;
    fAttributes.removeAllAttributes();
    do {
        // end tag?  
        int c = fEntityScanner.peekChar();
        if (c == '>') {
            fEntityScanner.scanChar();
            break;
        } else if (c == '/') {
            fEntityScanner.scanChar();
            if (!fEntityScanner.skipChar('>')) {
                reportFatalError("ElementUnterminated", new Object[] { rawname });
            }
            empty = true;
            break;
        } else if (!isValidNameStartChar(c) || !fSawSpace) {
            // Second chance. Check if this character is a high  
            // surrogate of a valid name start character.  
            if (!isValidNameStartHighSurrogate(c) || !fSawSpace) {
                reportFatalError("ElementUnterminated", new Object[] { rawname });
            }
        }
        // attributes  
        scanAttribute(fAttributes);
        // spaces  
        fSawSpace = fEntityScanner.skipSpaces();
    } while (true);
    // call handler  
    if (fDocumentHandler != null) {
        if (empty) {
            //decrease the markup depth..  
            fMarkupDepth--;
            // check that this element was opened in the same entity  
            if (fMarkupDepth < fEntityStack[fEntityDepth - 1]) {
                reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
            }
            fDocumentHandler.emptyElement(fElementQName, fAttributes, null);
            //pop the element off the stack..  
            fElementStack.popElement(fElementQName);
        } else {
            fDocumentHandler.startElement(fElementQName, fAttributes, null);
        }
    }
    if (DEBUG_CONTENT_SCANNING)
        System.out.println("<<< scanStartElementAfterName(): " + empty);
    return empty;
}
