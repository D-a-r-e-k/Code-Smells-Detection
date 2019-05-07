/**
     * Report the start of a CDATA section.
     * 
     * @throws org.xml.sax.SAXException The application may raise an exception.
     * @see #endCDATA
     */
public void startCDATA() throws org.xml.sax.SAXException {
    m_cdataStartCalled = true;
}
