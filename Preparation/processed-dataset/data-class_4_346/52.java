/**
     * Report the end of a CDATA section.
     * @throws org.xml.sax.SAXException The application may raise an exception.
     *
     *  @see  #startCDATA
     */
public void endCDATA() throws org.xml.sax.SAXException {
    if (m_cdataTagOpen)
        closeCDATA();
    m_cdataStartCalled = false;
}
