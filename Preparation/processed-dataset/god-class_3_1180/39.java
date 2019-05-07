/**
   * Report an XML comment anywhere in the document.
   *
   * <p>This callback will be used for comments inside or outside the
   * document element, including comments in the external DTD
   * subset (if read).</p>
   *
   * @param ch An array holding the characters in the comment.
   * @param start The starting position in the array.
   * @param length The number of characters to use from the array.
   * @throws SAXException The application may raise an exception.
   */
public void comment(char ch[], int start, int length) throws SAXException {
    flushStartDoc();
    if (null != m_resultLexicalHandler)
        m_resultLexicalHandler.comment(ch, start, length);
}
