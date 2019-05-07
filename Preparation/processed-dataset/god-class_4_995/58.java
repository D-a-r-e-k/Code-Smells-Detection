/**
     *  Return CHARACTER, if you think this was a plain character; ELEMENT, if
     *  you think this was a wiki markup element, and IGNORE, if you think
     *  we should ignore this altogether.
     *  <p>
     *  To add your own MarkupParser, you can override this method, but it
     *  is recommended that you call super.parseToken() as well to gain advantage
     *  of JSPWiki's own markup.  You can call it at the start of your own
     *  parseToken() or end - it does not matter.
     *
     * @param ch The character under investigation
     * @return {@link #ELEMENT}, {@link #CHARACTER} or {@link #IGNORE}.
     * @throws IOException If parsing fails.
     */
protected int parseToken(int ch) throws IOException {
    Element el = null;
    // 
    //  Now, check the incoming token. 
    // 
    switch(ch) {
        case '\r':
            // DOS linefeeds we forget 
            return IGNORE;
        case '\n':
            // 
            //  Close things like headings, etc. 
            // 
            // FIXME: This is not really very fast 
            closeHeadings();
            popElement("dl");
            // Close definition lists. 
            if (m_istable) {
                popElement("tr");
            }
            m_isdefinition = false;
            if (m_newLine) {
                // Paragraph change. 
                startBlockLevel();
                // 
                //  Figure out which elements cannot be enclosed inside 
                //  a <p></p> pair according to XHTML rules. 
                // 
                String nextLine = peekAheadLine();
                if (nextLine.length() == 0 || (nextLine.length() > 0 && !nextLine.startsWith("{{{") && !nextLine.startsWith("----") && !nextLine.startsWith("%%") && "*#!;".indexOf(nextLine.charAt(0)) == -1)) {
                    pushElement(new Element("p"));
                    m_isOpenParagraph = true;
                    if (m_restartitalic) {
                        pushElement(new Element("i"));
                        m_isitalic = true;
                        m_restartitalic = false;
                    }
                    if (m_restartbold) {
                        pushElement(new Element("b"));
                        m_isbold = true;
                        m_restartbold = false;
                    }
                }
            } else {
                m_plainTextBuf.append("\n");
                m_newLine = true;
            }
            return IGNORE;
        case '\\':
            el = handleBackslash();
            break;
        case '_':
            el = handleUnderscore();
            break;
        case '\'':
            el = handleApostrophe();
            break;
        case '{':
            el = handleOpenbrace(m_newLine);
            break;
        case '}':
            el = handleClosebrace();
            break;
        case '-':
            if (m_newLine)
                el = handleDash();
            break;
        case '!':
            if (m_newLine) {
                el = handleHeading();
            }
            break;
        case ';':
            if (m_newLine) {
                el = handleDefinitionList();
            }
            break;
        case ':':
            if (m_isdefinition) {
                popElement("dt");
                el = pushElement(new Element("dd"));
                m_isdefinition = false;
            }
            break;
        case '[':
            el = handleOpenbracket();
            break;
        case '*':
            if (m_newLine) {
                pushBack('*');
                el = handleGeneralList();
            }
            break;
        case '#':
            if (m_newLine) {
                pushBack('#');
                el = handleGeneralList();
            }
            break;
        case '|':
            el = handleBar(m_newLine);
            break;
        case '~':
            el = handleTilde();
            break;
        case '%':
            el = handleDiv(m_newLine);
            break;
        case '/':
            el = handleSlash(m_newLine);
            break;
        default:
            break;
    }
    return el != null ? ELEMENT : CHARACTER;
}
