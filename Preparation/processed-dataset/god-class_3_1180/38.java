/**
   * Report the end of a CDATA section.
   *
   * @throws SAXException The application may raise an exception.
   * @see #startCDATA
   */
public void endCDATA() throws SAXException {
    if (null != m_resultLexicalHandler)
        m_resultLexicalHandler.endCDATA();
}
