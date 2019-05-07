private int flushPlainText() {
    int numChars = m_plainTextBuf.length();
    if (numChars > 0) {
        String buf;
        if (!m_allowHTML) {
            buf = escapeHTMLEntities(m_plainTextBuf.toString());
        } else {
            buf = m_plainTextBuf.toString();
        }
        // 
        //  We must first empty the buffer because the side effect of 
        //  calling makeCamelCaseLink() is to call this routine. 
        // 
        m_plainTextBuf = new StringBuilder(20);
        try {
            // 
            //  This is the heaviest part of parsing, and therefore we can 
            //  do some optimization here. 
            // 
            //  1) Only when the length of the buffer is big enough, we try to do the match 
            // 
            if (m_camelCaseLinks && !m_isEscaping && buf.length() > 3) {
                // System.out.println("Buffer="+buf); 
                while (m_camelCaseMatcher.contains(buf, m_camelCasePattern)) {
                    MatchResult result = m_camelCaseMatcher.getMatch();
                    String firstPart = buf.substring(0, result.beginOffset(0));
                    String prefix = result.group(1);
                    if (prefix == null)
                        prefix = "";
                    String camelCase = result.group(2);
                    String protocol = result.group(3);
                    String uri = protocol + result.group(4);
                    buf = buf.substring(result.endOffset(0));
                    m_currentElement.addContent(firstPart);
                    // 
                    //  Check if the user does not wish to do URL or WikiWord expansion 
                    // 
                    if (prefix.endsWith("~") || prefix.indexOf('[') != -1) {
                        if (prefix.endsWith("~")) {
                            if (m_wysiwygEditorMode) {
                                m_currentElement.addContent("~");
                            }
                            prefix = prefix.substring(0, prefix.length() - 1);
                        }
                        if (camelCase != null) {
                            m_currentElement.addContent(prefix + camelCase);
                        } else if (protocol != null) {
                            m_currentElement.addContent(prefix + uri);
                        }
                        continue;
                    }
                    // 
                    //  Fine, then let's check what kind of a link this was 
                    //  and emit the proper elements 
                    // 
                    if (protocol != null) {
                        char c = uri.charAt(uri.length() - 1);
                        if (c == '.' || c == ',') {
                            uri = uri.substring(0, uri.length() - 1);
                            buf = c + buf;
                        }
                        // System.out.println("URI match "+uri); 
                        m_currentElement.addContent(prefix);
                        makeDirectURILink(uri);
                    } else {
                        // System.out.println("Matched: '"+camelCase+"'"); 
                        // System.out.println("Split to '"+firstPart+"', and '"+buf+"'"); 
                        // System.out.println("prefix="+prefix); 
                        m_currentElement.addContent(prefix);
                        makeCamelCaseLink(camelCase);
                    }
                }
                m_currentElement.addContent(buf);
            } else {
                // 
                //  No camelcase asked for, just add the elements 
                // 
                m_currentElement.addContent(buf);
            }
        } catch (IllegalDataException e) {
            // 
            // Sometimes it's possible that illegal XML chars is added to the data. 
            // Here we make sure it does not stop parsing. 
            // 
            m_currentElement.addContent(makeError(cleanupSuspectData(e.getMessage())));
        }
    }
    return numChars;
}
