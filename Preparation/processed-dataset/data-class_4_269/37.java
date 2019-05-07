/**
     * Returns a value as a number.
     * http://www.w3.org/TR/SVGMobile12/types.html#DataTypeNumber
     */
private double toNumber(String str) throws IOException {
    return toLength(str, 100);
}
