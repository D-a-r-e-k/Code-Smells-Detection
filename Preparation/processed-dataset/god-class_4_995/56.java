private void fillBuffer(Element startElement) throws IOException {
    m_currentElement = startElement;
    boolean quitReading = false;
    m_newLine = true;
    disableOutputEscaping();
    while (!quitReading) {
        int ch = nextToken();
        if (ch == -1)
            break;
        // 
        //  Check if we're actually ending the preformatted mode. 
        //  We still must do an entity transformation here. 
        // 
        if (m_isEscaping) {
            if (ch == '}') {
                if (handleClosebrace() == null)
                    m_plainTextBuf.append((char) ch);
            } else if (ch == -1) {
                quitReading = true;
            } else if (ch == '\r') {
            } else if (ch == '<') {
                m_plainTextBuf.append("&lt;");
            } else if (ch == '>') {
                m_plainTextBuf.append("&gt;");
            } else if (ch == '&') {
                m_plainTextBuf.append("&amp;");
            } else if (ch == '~') {
                String braces = readWhile("}");
                if (braces.length() >= 3) {
                    m_plainTextBuf.append("}}}");
                    braces = braces.substring(3);
                } else {
                    m_plainTextBuf.append((char) ch);
                }
                for (int i = braces.length() - 1; i >= 0; i--) {
                    pushBack(braces.charAt(i));
                }
            } else {
                m_plainTextBuf.append((char) ch);
            }
            continue;
        }
        // 
        //  An empty line stops a list 
        // 
        if (m_newLine && ch != '*' && ch != '#' && ch != ' ' && m_genlistlevel > 0) {
            m_plainTextBuf.append(unwindGeneralList());
        }
        if (m_newLine && ch != '|' && m_istable) {
            popElement("table");
            m_istable = false;
        }
        int skip = IGNORE;
        // 
        //  Do the actual parsing and catch any errors. 
        // 
        try {
            skip = parseToken(ch);
        } catch (IllegalDataException e) {
            log.info("Page " + m_context.getPage().getName() + " contains data which cannot be added to DOM tree: " + e.getMessage());
            makeError("Error: " + cleanupSuspectData(e.getMessage()));
        }
        // 
        //   The idea is as follows:  If the handler method returns 
        //   an element (el != null), it is assumed that it has been 
        //   added in the stack.  Otherwise the character is added 
        //   as is to the plaintext buffer. 
        // 
        //   For the transition phase, if s != null, it also gets 
        //   added in the plaintext buffer. 
        // 
        switch(skip) {
            case ELEMENT:
                m_newLine = false;
                break;
            case CHARACTER:
                m_plainTextBuf.append((char) ch);
                m_newLine = false;
                break;
            case IGNORE:
            default:
                break;
        }
    }
    closeHeadings();
    popElement("domroot");
}
