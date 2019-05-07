/**
     * Output the doc type declaration.
     *
     * @param name non-null reference to document type name.
     * NEEDSDOC @param value
     *
     * @throws org.xml.sax.SAXException
     */
void outputEntityDecl(String name, String value) throws IOException {
    final java.io.Writer writer = m_writer;
    writer.write("<!ENTITY ");
    writer.write(name);
    writer.write(" \"");
    writer.write(value);
    writer.write("\">");
    writer.write(m_lineSep, 0, m_lineSepLen);
}
