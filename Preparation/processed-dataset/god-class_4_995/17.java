/**
     *  Escapes XML entities in a HTML-compatible way (i.e. does not escape
     *  entities that are already escaped).
     *
     *  @param buf
     *  @return An escaped string.
     */
private String escapeHTMLEntities(String buf) {
    StringBuilder tmpBuf = new StringBuilder(buf.length() + 20);
    for (int i = 0; i < buf.length(); i++) {
        char ch = buf.charAt(i);
        if (ch == '<') {
            tmpBuf.append("&lt;");
        } else if (ch == '>') {
            tmpBuf.append("&gt;");
        } else if (ch == '\"') {
            tmpBuf.append("&quot;");
        } else if (ch == '&') {
            // 
            //  If the following is an XML entity reference (&#.*;) we'll 
            //  leave it as it is; otherwise we'll replace it with an &amp; 
            // 
            boolean isEntity = false;
            StringBuilder entityBuf = new StringBuilder();
            if (i < buf.length() - 1) {
                for (int j = i; j < buf.length(); j++) {
                    char ch2 = buf.charAt(j);
                    if (Character.isLetterOrDigit(ch2) || (ch2 == '#' && j == i + 1) || ch2 == ';' || ch2 == '&') {
                        entityBuf.append(ch2);
                        if (ch2 == ';') {
                            isEntity = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (isEntity) {
                tmpBuf.append(entityBuf);
                i = i + entityBuf.length() - 1;
            } else {
                tmpBuf.append("&amp;");
            }
        } else {
            tmpBuf.append(ch);
        }
    }
    return tmpBuf.toString();
}
