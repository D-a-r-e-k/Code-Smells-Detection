private static String trunc(String str, int length) {
    if (str.length() > length) {
        return str.substring(0, length);
    } else {
        return str;
    }
}
