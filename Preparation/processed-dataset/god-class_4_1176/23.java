/**
     * Report a parsed external entity declaration.
     *
     * <p>Only the effective (first) declaration for each entity
     * will be reported.</p>
     *
     * @param name The name of the entity.  If it is a parameter
     *        entity, the name will begin with '%'.
     * @param publicId The declared public identifier of the entity, or
     *        null if none was declared.
     * @param systemId The declared system identifier of the entity.
     * @exception SAXException The application may raise an exception.
     * @see #internalEntityDecl
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl
     */
public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
    try {
        DTDprolog();
        m_writer.write("<!ENTITY ");
        m_writer.write(name);
        if (publicId != null) {
            m_writer.write(" PUBLIC \"");
            m_writer.write(publicId);
        } else {
            m_writer.write(" SYSTEM \"");
            m_writer.write(systemId);
        }
        m_writer.write("\" >");
        m_writer.write(m_lineSep, 0, m_lineSepLen);
    } catch (IOException e) {
        // TODO Auto-generated catch block  
        e.printStackTrace();
    }
}
