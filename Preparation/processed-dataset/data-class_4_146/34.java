/**
     * ErrorHandler interface.
     * 
     * Receive notification of a warning.
     * 
     * @param e
     *          The error information encapsulated in a SAX parse exception.
     * @exception SAXException
     *              Any SAX exception, possibly wrapping another exception.
     */
public void warning(SAXParseException e) throws SAXException {
    addValidationException(e);
}
