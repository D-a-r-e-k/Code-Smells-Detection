/**
     *  Starts a block level element, therefore closing
     *  a potential open paragraph tag.
     */
private void startBlockLevel() {
    // These may not continue over block level limits in XHTML 
    popElement("i");
    popElement("b");
    popElement("tt");
    if (m_isOpenParagraph) {
        m_isOpenParagraph = false;
        popElement("p");
        m_plainTextBuf.append("\n");
    }
    m_restartitalic = m_isitalic;
    m_restartbold = m_isbold;
    m_isitalic = false;
    m_isbold = false;
}
