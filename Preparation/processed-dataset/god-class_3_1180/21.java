////////////////////////////////////////////////////////////////////  
// Default implementation of ContentHandler interface.  
////////////////////////////////////////////////////////////////////  
/**
   * Receive a Locator object for document events.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass if they wish to store the locator for use
   * with other document events.</p>
   *
   * @param locator A locator for all SAX document events.
   * @see org.xml.sax.ContentHandler#setDocumentLocator
   * @see org.xml.sax.Locator
   */
public void setDocumentLocator(Locator locator) {
    try {
        if (null == m_resultContentHandler)
            createResultContentHandler(m_result);
    } catch (TransformerException te) {
        throw new org.apache.xml.utils.WrappedRuntimeException(te);
    }
    m_resultContentHandler.setDocumentLocator(locator);
}
