/**
     * For the enclosing elements starting tag write out
     * out any attributes followed by ">"
     *
     * @throws org.xml.sax.SAXException
     */
protected void closeStartTag() throws SAXException {
    if (m_elemContext.m_startTagOpen) {
        try {
            if (m_tracer != null)
                super.fireStartElem(m_elemContext.m_elementName);
            int nAttrs = m_attributes.getLength();
            if (nAttrs > 0) {
                processAttributes(m_writer, nAttrs);
                // clear attributes object for re-use with next element  
                m_attributes.clear();
            }
            m_writer.write('>');
        } catch (IOException e) {
            throw new SAXException(e);
        }
        /* whether Xalan or XSLTC, we have the prefix mappings now, so
             * lets determine if the current element is specified in the cdata-
             * section-elements list.
             */
        if (m_CdataElems != null)
            m_elemContext.m_isCdataSection = isCdataSection();
        if (m_doIndent) {
            m_isprevtext = false;
            m_preserves.push(m_ispreserve);
        }
    }
}
