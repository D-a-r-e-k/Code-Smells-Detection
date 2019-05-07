// normalize the string according to the whiteSpace facet  
public static String normalize(String content, short ws) {
    int len = content == null ? 0 : content.length();
    if (len == 0 || ws == XSSimpleType.WS_PRESERVE)
        return content;
    StringBuffer sb = new StringBuffer();
    if (ws == XSSimpleType.WS_REPLACE) {
        char ch;
        // when it's replace, just replace #x9, #xa, #xd by #x20  
        for (int i = 0; i < len; i++) {
            ch = content.charAt(i);
            if (ch != 0x9 && ch != 0xa && ch != 0xd)
                sb.append(ch);
            else
                sb.append((char) 0x20);
        }
    } else {
        char ch;
        int i;
        boolean isLeading = true;
        // when it's collapse  
        for (i = 0; i < len; i++) {
            ch = content.charAt(i);
            // append real characters, so we passed leading ws  
            if (ch != 0x9 && ch != 0xa && ch != 0xd && ch != 0x20) {
                sb.append(ch);
                isLeading = false;
            } else {
                // for whitespaces, we skip all following ws  
                for (; i < len - 1; i++) {
                    ch = content.charAt(i + 1);
                    if (ch != 0x9 && ch != 0xa && ch != 0xd && ch != 0x20)
                        break;
                }
                // if it's not a leading or tailing ws, then append a space  
                if (i < len - 1 && !isLeading)
                    sb.append((char) 0x20);
            }
        }
    }
    return sb.toString();
}
