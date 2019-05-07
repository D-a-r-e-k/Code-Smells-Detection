/**
     * Ends an un-escaping section.
     *
     * @see #startNonEscaping
     *
     * @throws org.xml.sax.SAXException
     */
public void endNonEscaping() throws org.xml.sax.SAXException {
    m_disableOutputEscapingStates.pop();
}
