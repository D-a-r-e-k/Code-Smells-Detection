private static int parseInteger(String s) {
    if (s.startsWith("0x") || s.startsWith("0X")) {
        return Integer.parseInt(s.substring(2), 16);
    } else {
        return Integer.parseInt(s);
    }
}
