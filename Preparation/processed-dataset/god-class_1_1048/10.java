//------------------------------------------------------------------------ 
/**
     * Replaces occurences of non-alphanumeric characters with a
     * supplied char.
     */
public static String replaceNonAlphanumeric(String str, char subst) {
    StringBuffer ret = new StringBuffer(str.length());
    char[] testChars = str.toCharArray();
    for (int i = 0; i < testChars.length; i++) {
        if (Character.isLetterOrDigit(testChars[i])) {
            ret.append(testChars[i]);
        } else {
            ret.append(subst);
        }
    }
    return ret.toString();
}
