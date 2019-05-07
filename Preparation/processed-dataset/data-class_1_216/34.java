/**
     * @param tag
     * @return
     */
public static String stripInvalidTagCharacters(String tag) {
    if (tag == null)
        throw new NullPointerException();
    StringBuffer sb = new StringBuffer();
    char[] charArray = tag.toCharArray();
    for (int i = 0; i < charArray.length; i++) {
        char c = charArray[i];
        // fast-path exclusions quotes and commas are obvious 
        switch(c) {
            case 34:
            // " 
            case 44:
                // , 
                continue;
        }
        if ((33 <= c && c <= 126) || Character.isUnicodeIdentifierPart(c) || Character.isUnicodeIdentifierStart(c)) {
            sb.append(charArray[i]);
        }
    }
    return sb.toString();
}
