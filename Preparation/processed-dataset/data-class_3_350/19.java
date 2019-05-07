////////////////////////////////////////////////////////////////////  
// Default implementation of DTDHandler interface.  
////////////////////////////////////////////////////////////////////  
/**
   * Receive notification of a notation declaration.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass if they wish to keep track of the notations
   * declared in a document.</p>
   *
   * @param name The notation name.
   * @param publicId The notation public identifier, or null if not
   *                 available.
   * @param systemId The notation system identifier.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.DTDHandler#notationDecl
   *
   * @throws SAXException
   */
public void notationDecl(String name, String publicId, String systemId) throws SAXException {
    if (null != m_resultDTDHandler)
        m_resultDTDHandler.notationDecl(name, publicId, systemId);
}
