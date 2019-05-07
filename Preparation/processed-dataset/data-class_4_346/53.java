/**
     * Report the end of DTD declarations.
     * @throws org.xml.sax.SAXException The application may raise an exception.
     * @see #startDTD
     */
public void endDTD() throws org.xml.sax.SAXException {
    try {
        if (m_needToOutputDocTypeDecl) {
            outputDocTypeDecl(m_elemContext.m_elementName, false);
            m_needToOutputDocTypeDecl = false;
        }
        final java.io.Writer writer = m_writer;
        if (!m_inDoctype)
            writer.write("]>");
        else {
            writer.write('>');
        }
        writer.write(m_lineSep, 0, m_lineSepLen);
    } catch (IOException e) {
        throw new SAXException(e);
    }
}
