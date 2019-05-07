/**
     * Output the doc type declaration.
     *
     * @param name non-null reference to document type name.
     * NEEDSDOC @param closeDecl
     *
     * @throws java.io.IOException
     */
void outputDocTypeDecl(String name, boolean closeDecl) throws SAXException {
    if (m_cdataTagOpen)
        closeCDATA();
    try {
        final java.io.Writer writer = m_writer;
        writer.write("<!DOCTYPE ");
        writer.write(name);
        String doctypePublic = getDoctypePublic();
        if (null != doctypePublic) {
            writer.write(" PUBLIC \"");
            writer.write(doctypePublic);
            writer.write('\"');
        }
        String doctypeSystem = getDoctypeSystem();
        if (null != doctypeSystem) {
            if (null == doctypePublic)
                writer.write(" SYSTEM \"");
            else
                writer.write(" \"");
            writer.write(doctypeSystem);
            if (closeDecl) {
                writer.write("\">");
                writer.write(m_lineSep, 0, m_lineSepLen);
                closeDecl = false;
            } else
                writer.write('\"');
        }
    } catch (IOException e) {
        throw new SAXException(e);
    }
}
