String extract(String text, String pattern, String defaultColor) {
    int index = text.indexOf(pattern);
    if (index < 0)
        return defaultColor;
    index += pattern.length();
    return decode(text.substring(index, index + 7));
}
