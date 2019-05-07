/**
     * Receive notification of character data.
     *
     * @param s The string of characters to process.
     *
     * @throws org.xml.sax.SAXException
     */
public void characters(String s) throws org.xml.sax.SAXException {
    if (m_inEntityRef && !m_expandDTDEntities)
        return;
    final int length = s.length();
    if (length > m_charsBuff.length) {
        m_charsBuff = new char[length * 2 + 1];
    }
    s.getChars(0, length, m_charsBuff, 0);
    characters(m_charsBuff, 0, length);
}
