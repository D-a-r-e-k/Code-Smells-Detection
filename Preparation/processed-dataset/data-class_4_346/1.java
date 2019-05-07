/**
     * This helper method to writes out "]]>" when closing a CDATA section.
     *
     * @throws org.xml.sax.SAXException
     */
protected void closeCDATA() throws org.xml.sax.SAXException {
    try {
        m_writer.write(CDATA_DELIMITER_CLOSE);
        // write out a CDATA section closing "]]>"  
        m_cdataTagOpen = false;
    } catch (IOException e) {
        throw new SAXException(e);
    }
}
