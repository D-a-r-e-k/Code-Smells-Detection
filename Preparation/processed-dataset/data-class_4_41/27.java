// expandSystemId(String,String):String 
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
    // Windows fix 
    if (str.length() >= 2) {
        char ch1 = str.charAt(1);
        // change "C:blah" to "/C:blah" 
        if (ch1 == ':') {
            char ch0 = Character.toUpperCase(str.charAt(0));
            if (ch0 >= 'A' && ch0 <= 'Z') {
                str = "/" + str;
            }
        } else if (ch1 == '/' && str.charAt(0) == '/') {
            str = "file:" + str;
        }
    }
    // done 
    return str;
}
