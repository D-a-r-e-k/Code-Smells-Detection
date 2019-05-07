/**
     * Process the attributes, which means to write out the currently
     * collected attributes to the writer. The attributes are not
     * cleared by this method
     * 
     * @param writer the writer to write processed attributes to.
     * @param nAttrs the number of attributes in m_attributes 
     * to be processed
     *
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
public void processAttributes(java.io.Writer writer, int nAttrs) throws IOException, SAXException {
    /* real SAX attributes are not passed in, so process the 
             * attributes that were collected after the startElement call.
             * _attribVector is a "cheap" list for Stream serializer output
             * accumulated over a series of calls to attribute(name,value)
             */
    String encoding = getEncoding();
    for (int i = 0; i < nAttrs; i++) {
        // elementAt is JDK 1.1.8  
        final String name = m_attributes.getQName(i);
        final String value = m_attributes.getValue(i);
        writer.write(' ');
        writer.write(name);
        writer.write("=\"");
        writeAttrString(writer, value, encoding);
        writer.write('\"');
    }
}
