protected final void flushStartDoc() throws SAXException {
    if (!m_flushedStartDoc) {
        if (m_resultContentHandler == null) {
            try {
                createResultContentHandler(m_result);
            } catch (TransformerException te) {
                throw new SAXException(te);
            }
        }
        m_resultContentHandler.startDocument();
        m_flushedStartDoc = true;
    }
}
