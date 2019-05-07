/**
   * Reset the status of the transformer.
   */
public void reset() {
    m_flushedStartDoc = false;
    m_foundFirstElement = false;
    m_outputStream = null;
    clearParameters();
    m_result = null;
    m_resultContentHandler = null;
    m_resultDeclHandler = null;
    m_resultDTDHandler = null;
    m_resultLexicalHandler = null;
    m_serializer = null;
    m_systemID = null;
    m_URIResolver = null;
    m_outputFormat = new OutputProperties(Method.XML);
}
