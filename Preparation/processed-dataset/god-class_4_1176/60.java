/**
     * Report the start of DTD declarations, if any.
     *
     * Any declarations are assumed to be in the internal subset unless
     * otherwise indicated.
     * 
     * @param name The document type name.
     * @param publicId The declared public identifier for the
     *        external DTD subset, or null if none was declared.
     * @param systemId The declared system identifier for the
     *        external DTD subset, or null if none was declared.
     * @throws org.xml.sax.SAXException The application may raise an
     *            exception.
     * @see #endDTD
     * @see #startEntity
     */
public void startDTD(String name, String publicId, String systemId) throws org.xml.sax.SAXException {
    setDoctypeSystem(systemId);
    setDoctypePublic(publicId);
    m_elemContext.m_elementName = name;
    m_inDoctype = true;
}
