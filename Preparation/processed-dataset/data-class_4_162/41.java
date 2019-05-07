/**
     *  Handles both }} and }}}
     */
private Element handleClosebrace() throws IOException {
    int ch2 = nextToken();
    if (ch2 == '}') {
        int ch3 = nextToken();
        if (ch3 == '}') {
            if (m_isPre) {
                if (m_isPreBlock) {
                    popElement("pre");
                } else {
                    popElement("span");
                }
                m_isPre = false;
                m_isEscaping = false;
                return m_currentElement;
            }
            m_plainTextBuf.append("}}}");
            return m_currentElement;
        }
        pushBack(ch3);
        if (!m_isEscaping) {
            return popElement("tt");
        }
    }
    pushBack(ch2);
    return null;
}
