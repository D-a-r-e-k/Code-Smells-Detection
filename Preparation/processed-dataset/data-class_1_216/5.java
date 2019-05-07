//------------------------------------------------------------------------ 
/**
     * Remove occurences of html, defined as any text
     * between the characters "&lt;" and "&gt;".  Replace
     * any HTML tags with a space.
     */
public static String removeHTML(String str) {
    return removeHTML(str, true);
}
