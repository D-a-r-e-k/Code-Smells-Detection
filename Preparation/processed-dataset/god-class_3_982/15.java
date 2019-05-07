protected int skipWhiteSpace(int i, String s) {
    for (; i < s.length() && (s.charAt(i) == ' ' || s.charAt(i) == '\t'); i++) {
        ;
    }
    return i;
}
