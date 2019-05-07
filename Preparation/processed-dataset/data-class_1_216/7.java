//------------------------------------------------------------------------ 
/** Run both removeHTML and escapeHTML on a string.
     * @param s String to be run through removeHTML and escapeHTML.
     * @return String with HTML removed and HTML special characters escaped.
     */
public static String removeAndEscapeHTML(String s) {
    if (s == null)
        return "";
    else
        return Utilities.escapeHTML(Utilities.removeHTML(s));
}
