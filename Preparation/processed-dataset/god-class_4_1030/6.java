private static String clean(String value) {
    if (value == null)
        return "";
    else
        return StringEscapeUtils.unescapeHtml(value.replaceAll("&nbsp;", " ").trim());
}
