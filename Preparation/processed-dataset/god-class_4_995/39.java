/**
     *  For example: italics.
     */
private Element handleApostrophe() throws IOException {
    int ch = nextToken();
    Element el = null;
    if (ch == '\'') {
        if (m_isitalic) {
            el = popElement("i");
        } else {
            el = pushElement(new Element("i"));
        }
        m_isitalic = !m_isitalic;
    } else {
        pushBack(ch);
    }
    return el;
}
