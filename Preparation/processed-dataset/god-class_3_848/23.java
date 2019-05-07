public static String escape(String string) {
    string = string.replaceAll("&", "&amp;");
    string = string.replaceAll("<", "&lt;");
    string = string.replaceAll("  ", " &nbsp;");
    string = string.replaceAll("\r\n", "<br />");
    string = string.replaceAll("\r", "<br />");
    string = string.replaceAll("\n", "<br />");
    return string;
}
