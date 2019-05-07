/**
     * If available, when the disable-output-escaping attribute is used,
     * output raw text without escaping.
     *
     * @param ch The characters from the XML document.
     * @param start The start position in the array.
     * @param length The number of characters to read from the array.
     *
     * @throws org.xml.sax.SAXException
     */
protected void charactersRaw(char ch[], int start, int length) throws org.xml.sax.SAXException {
    if (m_inEntityRef)
        return;
    try {
        if (m_elemContext.m_startTagOpen) {
            closeStartTag();
            m_elemContext.m_startTagOpen = false;
        }
        m_ispreserve = true;
        m_writer.write(ch, start, length);
    } catch (IOException e) {
        throw new SAXException(e);
    }
}
