/**
     * Receive notification of the beginning of an element, although this is a
     * SAX method additional namespace or attribute information can occur before
     * or after this call, that is associated with this element.
     *
     *
     * @param namespaceURI The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param name The element type name.
     * @param atts The attributes attached to the element, if any.
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startElement
     * @see org.xml.sax.ContentHandler#endElement
     * @see org.xml.sax.AttributeList
     *
     * @throws org.xml.sax.SAXException
     */
public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws org.xml.sax.SAXException {
    if (m_inEntityRef)
        return;
    if (m_needToCallStartDocument) {
        startDocumentInternal();
        m_needToCallStartDocument = false;
        m_docIsEmpty = false;
    } else if (m_cdataTagOpen)
        closeCDATA();
    try {
        if (m_needToOutputDocTypeDecl) {
            if (null != getDoctypeSystem()) {
                outputDocTypeDecl(name, true);
            }
            m_needToOutputDocTypeDecl = false;
        }
        /* before we over-write the current elementLocalName etc.
             * lets close out the old one (if we still need to)
             */
        if (m_elemContext.m_startTagOpen) {
            closeStartTag();
            m_elemContext.m_startTagOpen = false;
        }
        if (namespaceURI != null)
            ensurePrefixIsDeclared(namespaceURI, name);
        m_ispreserve = false;
        if (shouldIndent() && m_startNewLine) {
            indent();
        }
        m_startNewLine = true;
        final java.io.Writer writer = m_writer;
        writer.write('<');
        writer.write(name);
    } catch (IOException e) {
        throw new SAXException(e);
    }
    // process the attributes now, because after this SAX call they might be gone  
    if (atts != null)
        addAttributes(atts);
    m_elemContext = m_elemContext.push(namespaceURI, localName, name);
    m_isprevtext = false;
    if (m_tracer != null)
        firePseudoAttributes();
}
