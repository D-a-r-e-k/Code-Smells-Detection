private Element handleOpenbracket() throws IOException {
    StringBuilder sb = new StringBuilder(40);
    int pos = getPosition();
    int ch = nextToken();
    boolean isPlugin = false;
    if (ch == '[') {
        if (m_wysiwygEditorMode) {
            sb.append('[');
        }
        sb.append((char) ch);
        while ((ch = nextToken()) == '[') {
            sb.append((char) ch);
        }
    }
    if (ch == '{') {
        isPlugin = true;
    }
    pushBack(ch);
    if (sb.length() > 0) {
        m_plainTextBuf.append(sb);
        return m_currentElement;
    }
    // 
    //  Find end of hyperlink 
    // 
    ch = nextToken();
    int nesting = 1;
    // Check for nested plugins 
    while (ch != -1) {
        int ch2 = nextToken();
        pushBack(ch2);
        if (isPlugin) {
            if (ch == '[' && ch2 == '{') {
                nesting++;
            } else if (nesting == 0 && ch == ']' && sb.charAt(sb.length() - 1) == '}') {
                break;
            } else if (ch == '}' && ch2 == ']') {
                // NB: This will be decremented once at the end 
                nesting--;
            }
        } else {
            if (ch == ']') {
                break;
            }
        }
        sb.append((char) ch);
        ch = nextToken();
    }
    // 
    //  If the link is never finished, do some tricks to display the rest of the line 
    //  unchanged. 
    // 
    if (ch == -1) {
        log.debug("Warning: unterminated link detected!");
        m_isEscaping = true;
        m_plainTextBuf.append(sb);
        flushPlainText();
        m_isEscaping = false;
        return m_currentElement;
    }
    return handleHyperlinks(sb.toString(), pos);
}
