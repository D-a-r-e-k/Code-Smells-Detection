private String escapeAscii(String text) {
    text = text.replaceAll("\\x0a", "\\\\n");
    text = text.replaceAll("\\x0d", "\\\\r");
    text = text.replaceAll("\\xa0", "\\\\u00a0");
    return text;
}
