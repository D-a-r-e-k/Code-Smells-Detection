/**
     * Returns a value as a height.
     * http://www.w3.org/TR/SVGMobile12/types.html#DataTypeLength
     */
private double toHeight(IXMLElement elem, String str) throws IOException {
    // XXX - Compute yPercentFactor from viewport 
    return toLength(elem, str, viewportStack.peek().heightPercentFactor);
}
