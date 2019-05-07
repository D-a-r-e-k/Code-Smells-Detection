/**
     *  Generic escape of next character or entity.
     */
private Element handleTilde() throws IOException {
    int ch = nextToken();
    if (ch == ' ') {
        if (m_wysiwygEditorMode) {
            m_plainTextBuf.append("~ ");
        }
        return m_currentElement;
    }
    if (ch == '|' || ch == '~' || ch == '\\' || ch == '*' || ch == '#' || ch == '-' || ch == '!' || ch == '\'' || ch == '_' || ch == '[' || ch == '{' || ch == ']' || ch == '}' || ch == '%') {
        if (m_wysiwygEditorMode) {
            m_plainTextBuf.append('~');
        }
        m_plainTextBuf.append((char) ch);
        m_plainTextBuf.append(readWhile("" + (char) ch));
        return m_currentElement;
    }
    // No escape. 
    pushBack(ch);
    return null;
}
