/**
   * Report the end of DTD declarations.
   *
   * @throws SAXException The application may raise an exception.
   * @see #startDTD
   */
public void endDTD() throws SAXException {
    if (null != m_resultLexicalHandler)
        m_resultLexicalHandler.endDTD();
}
