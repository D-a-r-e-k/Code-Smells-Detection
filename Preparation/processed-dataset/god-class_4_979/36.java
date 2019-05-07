/**
     * ErrorHandler interface.
     * 
     * Receive notification of a non-recoverable error.
     * 
     * @param e
     *          The error information encapsulated in a SAX parse exception.
     * @exception SAXException
     *              Any SAX exception, possibly wrapping another exception.
     */
public void fatalError(SAXParseException e) throws SAXException {
    addValidationException(e);
}
