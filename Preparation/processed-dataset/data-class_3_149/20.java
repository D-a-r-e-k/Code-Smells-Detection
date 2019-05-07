protected int getNumericValue(String s, int i) {
    int endOfVal = findNextWhiteSpace(i, s);
    String val = s.substring(i, endOfVal);
    return Integer.parseInt(val);
}
