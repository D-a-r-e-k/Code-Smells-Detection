/**
     * Report the beginning of an entity.
     * 
     * The start and end of the document entity are not reported.
     * The start and end of the external DTD subset are reported
     * using the pseudo-name "[dtd]".  All other events must be
     * properly nested within start/end entity events.
     * 
     * @param name The name of the entity.  If it is a parameter
     *        entity, the name will begin with '%'.
     * @throws org.xml.sax.SAXException The application may raise an exception.
     * @see #endEntity
     * @see org.xml.sax.ext.DeclHandler#internalEntityDecl
     * @see org.xml.sax.ext.DeclHandler#externalEntityDecl
     */
public void startEntity(String name) throws org.xml.sax.SAXException {
    if (name.equals("[dtd]"))
        m_inExternalDTD = true;
    if (!m_expandDTDEntities && !m_inExternalDTD) {
        /* Only leave the entity as-is if
             * we've been told not to expand them
             * and this is not the magic [dtd] name.
             */
        startNonEscaping();
        characters("&" + name + ';');
        endNonEscaping();
    }
    m_inEntityRef = true;
}
