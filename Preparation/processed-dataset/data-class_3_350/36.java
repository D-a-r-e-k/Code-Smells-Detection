/**
   * Report the end of an entity.
   *
   * @param name The name of the entity that is ending.
   * @throws SAXException The application may raise an exception.
   * @see #startEntity
   */
public void endEntity(String name) throws SAXException {
    if (null != m_resultLexicalHandler)
        m_resultLexicalHandler.endEntity(name);
}
