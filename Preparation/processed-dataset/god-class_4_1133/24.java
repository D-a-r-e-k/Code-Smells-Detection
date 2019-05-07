// scanCDATASection(boolean):boolean  
/**
     * Scans an end element.
     * <p>
     * <pre>
     * [42] ETag ::= '&lt;/' Name S? '>'
     * </pre>
     * <p>
     * <strong>Note:</strong> This method uses the fElementQName variable.
     * The contents of this variable will be destroyed. The caller should
     * copy the needed information out of this variable before calling
     * this method.
     *
     * @return The element depth.
     */
protected int scanEndElement() throws IOException, XNIException {
    if (DEBUG_CONTENT_SCANNING)
        System.out.println(">>> scanEndElement()");
    fElementStack.popElement(fElementQName);
    // Take advantage of the fact that next string _should_ be "fElementQName.rawName",  
    //In scanners most of the time is consumed on checks done for XML characters, we can  
    // optimize on it and avoid the checks done for endElement,  
    //we will also avoid symbol table lookup - neeraj.bajaj@sun.com  
    // this should work both for namespace processing true or false...  
    //REVISIT: if the string is not the same as expected.. we need to do better error handling..  
    //We can skip this for now... In any case if the string doesn't match -- document is not well formed.  
    if (!fEntityScanner.skipString(fElementQName.rawname)) {
        reportFatalError("ETagRequired", new Object[] { fElementQName.rawname });
    }
    // end  
    fEntityScanner.skipSpaces();
    if (!fEntityScanner.skipChar('>')) {
        reportFatalError("ETagUnterminated", new Object[] { fElementQName.rawname });
    }
    fMarkupDepth--;
    //we have increased the depth for two markup "<" characters  
    fMarkupDepth--;
    // check that this element was opened in the same entity  
    if (fMarkupDepth < fEntityStack[fEntityDepth - 1]) {
        reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
    }
    // call handler  
    if (fDocumentHandler != null) {
        fDocumentHandler.endElement(fElementQName, null);
    }
    return fMarkupDepth;
}
