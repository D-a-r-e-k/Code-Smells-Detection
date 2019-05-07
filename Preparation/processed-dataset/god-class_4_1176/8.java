/**
     * Output a system-dependent line break.
     *
     * @throws org.xml.sax.SAXException
     */
protected final void outputLineSep() throws IOException {
    m_writer.write(m_lineSep, 0, m_lineSepLen);
}
