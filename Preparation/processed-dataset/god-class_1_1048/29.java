public static String encodeEmail(String str) {
    return str != null ? RegexUtil.encodeEmail(str) : null;
}
