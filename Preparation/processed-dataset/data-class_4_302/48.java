// createLatin1Reader(InputStream):Reader  
//  
// Protected static methods  
//  
/**
     * Fixes a platform dependent filename to standard URI form.
     *
     * @param str The string to fix.
     *
     * @return Returns the fixed URI string.
     */
protected static String fixURI(String str) {
    // handle platform dependent strings  
    str = str.replace(java.io.File.separatorChar, '/');
    StringBuffer sb = null;
    // Windows fix  
    if (str.length() >= 2) {
        char ch1 = str.charAt(1);
        // change "C:blah" to "file:///C:blah"  
        if (ch1 == ':') {
            char ch0 = Character.toUpperCase(str.charAt(0));
            if (ch0 >= 'A' && ch0 <= 'Z') {
                sb = new StringBuffer(str.length() + 8);
                sb.append("file:///");
            }
        } else if (ch1 == '/' && str.charAt(0) == '/') {
            sb = new StringBuffer(str.length() + 5);
            sb.append("file:");
        }
    }
    int pos = str.indexOf(' ');
    // there is no space in the string  
    // we just append "str" to the end of sb  
    if (pos < 0) {
        if (sb != null) {
            sb.append(str);
            str = sb.toString();
        }
    } else {
        if (sb == null)
            sb = new StringBuffer(str.length());
        // put characters before ' ' into the string buffer  
        for (int i = 0; i < pos; i++) sb.append(str.charAt(i));
        // and %20 for the space  
        sb.append("%20");
        // for the remamining part, also convert ' ' to "%20".  
        for (int i = pos + 1; i < str.length(); i++) {
            if (str.charAt(i) == ' ')
                sb.append("%20");
            else
                sb.append(str.charAt(i));
        }
        str = sb.toString();
    }
    // done  
    return str;
}
