/**
     * Receive notification of the end of an element.
     *
     *
     * @param namespaceURI The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param name The element type name
     * @throws org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     *
     * @throws org.xml.sax.SAXException
     */
public void endElement(String namespaceURI, String localName, String name) throws org.xml.sax.SAXException {
    if (m_inEntityRef)
        return;
    // namespaces declared at the current depth are no longer valid  
    // so get rid of them      
    m_prefixMap.popNamespaces(m_elemContext.m_currentElemDepth, null);
    try {
        final java.io.Writer writer = m_writer;
        if (m_elemContext.m_startTagOpen) {
            if (m_tracer != null)
                super.fireStartElem(m_elemContext.m_elementName);
            int nAttrs = m_attributes.getLength();
            if (nAttrs > 0) {
                processAttributes(m_writer, nAttrs);
                // clear attributes object for re-use with next element  
                m_attributes.clear();
            }
            if (m_spaceBeforeClose)
                writer.write(" />");
            else
                writer.write("/>");
        } else {
            if (m_cdataTagOpen)
                closeCDATA();
            if (shouldIndent())
                indent(m_elemContext.m_currentElemDepth - 1);
            writer.write('<');
            writer.write('/');
            writer.write(name);
            writer.write('>');
        }
    } catch (IOException e) {
        throw new SAXException(e);
    }
    if (!m_elemContext.m_startTagOpen && m_doIndent) {
        m_ispreserve = m_preserves.isEmpty() ? false : m_preserves.pop();
    }
    m_isprevtext = false;
    // fire off the end element event  
    if (m_tracer != null)
        super.fireEndElem(name);
    m_elemContext = m_elemContext.m_prev;
}
