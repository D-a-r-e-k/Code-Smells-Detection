// scanComment()  
/** 
     * Scans a start element. This method will handle the binding of
     * namespace information and notifying the handler of the start
     * of the element.
     * <p>
     * <pre>
     * [44] EmptyElemTag ::= '&lt;' Name (S Attribute)* S? '/>'
     * [40] STag ::= '&lt;' Name (S Attribute)* S? '>'
     * </pre> 
     * <p>
     * <strong>Note:</strong> This method assumes that the leading
     * '&lt;' character has been consumed.
     * <p>
     * <strong>Note:</strong> This method uses the fElementQName and
     * fAttributes variables. The contents of these variables will be
     * destroyed. The caller should copy important information out of
     * these variables before calling this method.
     *
     * @return True if element is empty. (i.e. It matches
     *          production [44].
     */
protected boolean scanStartElement() throws IOException, XNIException {
    if (DEBUG_CONTENT_SCANNING)
        System.out.println(">>> scanStartElement()");
    // name  
    if (fNamespaces) {
        fEntityScanner.scanQName(fElementQName);
    } else {
        String name = fEntityScanner.scanName();
        fElementQName.setValues(null, name, name, null);
    }
    String rawname = fElementQName.rawname;
    // push element stack  
    fCurrentElement = fElementStack.pushElement(fElementQName);
    // attributes  
    boolean empty = false;
    fAttributes.removeAllAttributes();
    do {
        // spaces  
        boolean sawSpace = fEntityScanner.skipSpaces();
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
        } else if (!isValidNameStartChar(c) || !sawSpace) {
            // Second chance. Check if this character is a high  
            // surrogate of a valid name start character.  
            if (!isValidNameStartHighSurrogate(c) || !sawSpace) {
                reportFatalError("ElementUnterminated", new Object[] { rawname });
            }
        }
        // attributes  
        scanAttribute(fAttributes);
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
        System.out.println("<<< scanStartElement(): " + empty);
    return empty;
}
