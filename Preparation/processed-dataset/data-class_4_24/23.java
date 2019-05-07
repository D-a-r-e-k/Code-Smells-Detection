private static long parseLong(String s) {
    if (s.endsWith("l") || s.endsWith("L")) {
        s = s.substring(0, s.length() - 1);
    }
    if (s.startsWith("0x") || s.startsWith("0X")) {
        return Long.parseLong(s.substring(2), 16);
    } else {
        return Long.parseLong(s);
    }
}
