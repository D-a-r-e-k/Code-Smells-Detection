private Element handleUnderscore() throws IOException {
    int ch = nextToken();
    Element el = null;
    if (ch == '_') {
        if (m_isbold) {
            el = popElement("b");
        } else {
            el = pushElement(new Element("b"));
        }
        m_isbold = !m_isbold;
    } else {
        pushBack(ch);
    }
    return el;
}
