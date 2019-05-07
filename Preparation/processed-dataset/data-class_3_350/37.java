/**
   * Report the start of a CDATA section.
   *
   * <p>The contents of the CDATA section will be reported through
   * the regular {@link org.xml.sax.ContentHandler#characters
   * characters} event.</p>
   *
   * @throws SAXException The application may raise an exception.
   * @see #endCDATA
   */
public void startCDATA() throws SAXException {
    if (null != m_resultLexicalHandler)
        m_resultLexicalHandler.startCDATA();
}
