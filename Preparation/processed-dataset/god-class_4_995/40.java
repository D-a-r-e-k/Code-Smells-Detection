private Element handleOpenbrace(boolean isBlock) throws IOException {
    int ch = nextToken();
    if (ch == '{') {
        int ch2 = nextToken();
        if (ch2 == '{') {
            m_isPre = true;
            m_isEscaping = true;
            m_isPreBlock = isBlock;
            if (isBlock) {
                startBlockLevel();
                return pushElement(new Element("pre"));
            }
            return pushElement(new Element("span").setAttribute("style", "font-family:monospace; white-space:pre;"));
        }
        pushBack(ch2);
        return pushElement(new Element("tt"));
    }
    pushBack(ch);
    return null;
}
