/**
     * Returns the specified <var>string</var> after substituting <VAR>specials</VAR>,
     * and UTF-16 surrogates for chracter references <CODE>&amp;#xnn</CODE>.
     *
     * @param   string      String to convert to XML format.
     * @param   encoding    CURRENTLY NOT IMPLEMENTED.
     *
     * @throws java.io.IOException
     */
public void writeAttrString(Writer writer, String string, String encoding) throws IOException {
    final int len = string.length();
    if (len > m_attrBuff.length) {
        m_attrBuff = new char[len * 2 + 1];
    }
    string.getChars(0, len, m_attrBuff, 0);
    final char[] stringChars = m_attrBuff;
    for (int i = 0; i < len; i++) {
        char ch = stringChars[i];
        if (m_charInfo.shouldMapAttrChar(ch)) {
            // The character is supposed to be replaced by a String  
            // e.g.   '&'  -->  "&amp;"  
            // e.g.   '<'  -->  "&lt;"  
            accumDefaultEscape(writer, ch, i, stringChars, len, false, true);
        } else {
            if (0x0 <= ch && ch <= 0x1F) {
                // Range 0x00 through 0x1F inclusive  
                // This covers the non-whitespace control characters  
                // in the range 0x1 to 0x1F inclusive.  
                // It also covers the whitespace control characters in the same way:  
                // 0x9   TAB  
                // 0xA   NEW LINE  
                // 0xD   CARRIAGE RETURN  
                //  
                // We also cover 0x0 ... It isn't valid  
                // but we will output "&#0;"   
                // The default will handle this just fine, but this  
                // is a little performance boost to handle the more  
                // common TAB, NEW-LINE, CARRIAGE-RETURN  
                switch(ch) {
                    case CharInfo.S_HORIZONAL_TAB:
                        writer.write("&#9;");
                        break;
                    case CharInfo.S_LINEFEED:
                        writer.write("&#10;");
                        break;
                    case CharInfo.S_CARRIAGERETURN:
                        writer.write("&#13;");
                        break;
                    default:
                        writer.write("&#");
                        writer.write(Integer.toString(ch));
                        writer.write(';');
                        break;
                }
            } else if (ch < 0x7F) {
                // Range 0x20 through 0x7E inclusive  
                // Normal ASCII chars  
                writer.write(ch);
            } else if (ch <= 0x9F) {
                // Range 0x7F through 0x9F inclusive  
                // More control characters  
                writer.write("&#");
                writer.write(Integer.toString(ch));
                writer.write(';');
            } else if (ch == CharInfo.S_LINE_SEPARATOR) {
                // LINE SEPARATOR  
                writer.write("&#8232;");
            } else if (m_encodingInfo.isInEncoding(ch)) {
                // If the character is in the encoding, and  
                // not in the normal ASCII range, we also  
                // just write it out  
                writer.write(ch);
            } else {
                // This is a fallback plan, we should never get here  
                // but if the character wasn't previously handled  
                // (i.e. isn't in the encoding, etc.) then what  
                // should we do?  We choose to write out a character ref  
                writer.write("&#");
                writer.write(Integer.toString(ch));
                writer.write(';');
            }
        }
    }
}
