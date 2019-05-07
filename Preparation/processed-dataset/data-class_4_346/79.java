/**
     * A private helper method to output the 
     * @throws SAXException
     * @throws IOException
     */
private void DTDprolog() throws SAXException, IOException {
    final java.io.Writer writer = m_writer;
    if (m_needToOutputDocTypeDecl) {
        outputDocTypeDecl(m_elemContext.m_elementName, false);
        m_needToOutputDocTypeDecl = false;
    }
    if (m_inDoctype) {
        writer.write(" [");
        writer.write(m_lineSep, 0, m_lineSepLen);
        m_inDoctype = false;
    }
}
