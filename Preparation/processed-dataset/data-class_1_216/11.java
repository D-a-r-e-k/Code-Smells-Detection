//------------------------------------------------------------------------ 
/**
     * Remove occurences of non-alphanumeric characters.
     */
public static String removeNonAlphanumeric(String str) {
    StringBuffer ret = new StringBuffer(str.length());
    char[] testChars = str.toCharArray();
    for (int i = 0; i < testChars.length; i++) {
        // MR: Allow periods in page links 
        if (Character.isLetterOrDigit(testChars[i]) || testChars[i] == '.') {
            ret.append(testChars[i]);
        }
    }
    return ret.toString();
}
