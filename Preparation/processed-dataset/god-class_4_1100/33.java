/**
     * Returns a value as a number.
     * http://www.w3.org/TR/SVGMobile12/types.html#DataTypeNumber
     */
private double toNumber(IXMLElement elem, String str) throws IOException {
    return toLength(elem, str, viewportStack.peek().numberFactor);
}
