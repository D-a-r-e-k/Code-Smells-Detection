/**
     * This method flushes any pending events, which can be startDocument()
     * closing the opening tag of an element, or closing an open CDATA section.
     */
public void flushPending() throws SAXException {
    if (m_needToCallStartDocument) {
        startDocumentInternal();
        m_needToCallStartDocument = false;
    }
    if (m_elemContext.m_startTagOpen) {
        closeStartTag();
        m_elemContext.m_startTagOpen = false;
    }
    if (m_cdataTagOpen) {
        closeCDATA();
        m_cdataTagOpen = false;
    }
    if (m_writer != null) {
        try {
            m_writer.flush();
        } catch (IOException e) {
        }
    }
}
