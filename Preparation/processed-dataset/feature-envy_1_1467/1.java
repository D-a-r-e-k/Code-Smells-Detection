//------------------------------------------------------------------------ 
/**
     * Escape, but do not replace HTML.
     * The default behaviour is to escape ampersands.
     */
public static String escapeHTML(String s) {
    return escapeHTML(s, true);
}
