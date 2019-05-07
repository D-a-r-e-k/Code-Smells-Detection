//------------------------------------------------------------------------ 
/**
     * Autoformat.
     */
public static String autoformat(String s) {
    String ret = StringUtils.replace(s, "\n", "<br />");
    return ret;
}
