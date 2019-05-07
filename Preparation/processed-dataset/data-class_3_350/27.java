/**
   * Receive notification of the start of an element.
   *
   * <p>By default, do nothing.  Application writers may override this
   * method in a subclass to take specific actions at the start of
   * each element (such as allocating a new tree node or writing
   * output to a file).</p>
   *
   * @param uri The Namespace URI, or the empty string if the
   *        element has no Namespace URI or if Namespace
   *        processing is not being performed.
   * @param localName The local name (without prefix), or the
   *        empty string if Namespace processing is not being
   *        performed.
   * @param qName The qualified name (with prefix), or the
   *        empty string if qualified names are not available.
   * @param attributes The specified or defaulted attributes.
   * @throws org.xml.sax.SAXException Any SAX exception, possibly
   *            wrapping another exception.
   * @see org.xml.sax.ContentHandler#startElement
   *
   * @throws SAXException
   */
public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    if (!m_foundFirstElement && null != m_serializer) {
        m_foundFirstElement = true;
        Serializer newSerializer;
        try {
            newSerializer = SerializerSwitcher.switchSerializerIfHTML(uri, localName, m_outputFormat.getProperties(), m_serializer);
        } catch (TransformerException te) {
            throw new SAXException(te);
        }
        if (newSerializer != m_serializer) {
            try {
                m_resultContentHandler = newSerializer.asContentHandler();
            } catch (IOException ioe) // why?  
            {
                throw new SAXException(ioe);
            }
            if (m_resultContentHandler instanceof DTDHandler)
                m_resultDTDHandler = (DTDHandler) m_resultContentHandler;
            if (m_resultContentHandler instanceof LexicalHandler)
                m_resultLexicalHandler = (LexicalHandler) m_resultContentHandler;
            m_serializer = newSerializer;
        }
    }
    flushStartDoc();
    m_resultContentHandler.startElement(uri, localName, qName, attributes);
}
