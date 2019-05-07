// To escape the "user.dir" system property, by using %HH to represent  
// special ASCII characters: 0x00~0x1F, 0x7F, ' ', '<', '>', '#', '%'  
// and '"'. It's a static method, so needs to be synchronized.  
// this method looks heavy, but since the system property isn't expected  
// to change often, so in most cases, we only need to return the URI  
// that was escaped before.  
// According to the URI spec, non-ASCII characters (whose value >= 128)  
// need to be escaped too.  
// REVISIT: don't know how to escape non-ASCII characters, especially  
// which encoding to use. Leave them for now.  
private static synchronized URI getUserDir() throws URI.MalformedURIException {
    // get the user.dir property  
    String userDir = "";
    try {
        userDir = (String) AccessController.doPrivileged(GET_USER_DIR_SYSTEM_PROPERTY);
    } catch (SecurityException se) {
    }
    // return empty string if property value is empty string.  
    if (userDir.length() == 0)
        return new URI("file", "", "", null, null);
    // compute the new escaped value if the new property value doesn't  
    // match the previous one  
    if (gUserDirURI != null && userDir.equals(gUserDir)) {
        return gUserDirURI;
    }
    // record the new value as the global property value  
    gUserDir = userDir;
    char separator = java.io.File.separatorChar;
    userDir = userDir.replace(separator, '/');
    int len = userDir.length(), ch;
    StringBuffer buffer = new StringBuffer(len * 3);
    // change C:/blah to /C:/blah  
    if (len >= 2 && userDir.charAt(1) == ':') {
        ch = Character.toUpperCase(userDir.charAt(0));
        if (ch >= 'A' && ch <= 'Z') {
            buffer.append('/');
        }
    }
    // for each character in the path  
    int i = 0;
    for (; i < len; i++) {
        ch = userDir.charAt(i);
        // if it's not an ASCII character, break here, and use UTF-8 encoding  
        if (ch >= 128)
            break;
        if (gNeedEscaping[ch]) {
            buffer.append('%');
            buffer.append(gAfterEscaping1[ch]);
            buffer.append(gAfterEscaping2[ch]);
        } else {
            buffer.append((char) ch);
        }
    }
    // we saw some non-ascii character  
    if (i < len) {
        // get UTF-8 bytes for the remaining sub-string  
        byte[] bytes = null;
        byte b;
        try {
            bytes = userDir.substring(i).getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            // should never happen  
            return new URI("file", "", userDir, null, null);
        }
        len = bytes.length;
        // for each byte  
        for (i = 0; i < len; i++) {
            b = bytes[i];
            // for non-ascii character: make it positive, then escape  
            if (b < 0) {
                ch = b + 256;
                buffer.append('%');
                buffer.append(gHexChs[ch >> 4]);
                buffer.append(gHexChs[ch & 0xf]);
            } else if (gNeedEscaping[b]) {
                buffer.append('%');
                buffer.append(gAfterEscaping1[b]);
                buffer.append(gAfterEscaping2[b]);
            } else {
                buffer.append((char) b);
            }
        }
    }
    // change blah/blah to blah/blah/  
    if (!userDir.endsWith("/"))
        buffer.append('/');
    gUserDirURI = new URI("file", "", buffer.toString(), null, null);
    return gUserDirURI;
}
