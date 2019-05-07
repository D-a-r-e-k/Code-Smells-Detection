// handleCharacter(char)  
/** 
     * Handles the end element. This method will make sure that
     * the end element name matches the current element and notify
     * the handler about the end of the element and the end of any
     * relevent prefix mappings.
     * <p>
     * <strong>Note:</strong> This method uses the fQName variable.
     * The contents of this variable will be destroyed.
     *
     * @param element The element.
     *
     * @return The element depth.
     *
     * @throws XNIException Thrown if the handler throws a SAX exception
     *                      upon notification.
     *
     */
// REVISIT: need to remove this method. It's not called anymore, because  
// the handling is done when the end tag is scanned. - SG  
protected int handleEndElement(QName element, boolean isEmpty) throws XNIException {
    fMarkupDepth--;
    // check that this element was opened in the same entity  
    if (fMarkupDepth < fEntityStack[fEntityDepth - 1]) {
        reportFatalError("ElementEntityMismatch", new Object[] { fCurrentElement.rawname });
    }
    // make sure the elements match  
    QName startElement = fQName;
    fElementStack.popElement(startElement);
    if (element.rawname != startElement.rawname) {
        reportFatalError("ETagRequired", new Object[] { startElement.rawname });
    }
    // bind namespaces  
    if (fNamespaces) {
        element.uri = startElement.uri;
    }
    // call handler  
    if (fDocumentHandler != null && !isEmpty) {
        fDocumentHandler.endElement(element, null);
    }
    return fMarkupDepth;
}
