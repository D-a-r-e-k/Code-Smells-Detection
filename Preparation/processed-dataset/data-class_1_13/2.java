private String unescapeAscii(String text) {
    text = text.replaceAll("\\\\n", "\n");
    text = text.replaceAll("\\\\r", "\r");
    return text;
}
