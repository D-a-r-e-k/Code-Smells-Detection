// Implement DTDHandler  
/**
     * If this method is called, the serializer is used as a
     * DTDHandler, which changes behavior how the serializer 
     * handles document entities. 
     * @see org.xml.sax.DTDHandler#notationDecl(java.lang.String, java.lang.String, java.lang.String)
     */
public void notationDecl(String name, String pubID, String sysID) throws SAXException {
    // TODO Auto-generated method stub  
    try {
        DTDprolog();
        m_writer.write("<!NOTATION ");
        m_writer.write(name);
        if (pubID != null) {
            m_writer.write(" PUBLIC \"");
            m_writer.write(pubID);
        } else {
            m_writer.write(" SYSTEM \"");
            m_writer.write(sysID);
        }
        m_writer.write("\" >");
        m_writer.write(m_lineSep, 0, m_lineSepLen);
    } catch (IOException e) {
        // TODO Auto-generated catch block  
        e.printStackTrace();
    }
}
