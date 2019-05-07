/**
     * Returns a value as a width.
     * http://www.w3.org/TR/SVGMobile12/types.html#DataTypeLength
     */
private double toWidth(IXMLElement elem, String str) throws IOException {
    // XXX - Compute xPercentFactor from viewport 
    return toLength(elem, str, viewportStack.peek().widthPercentFactor);
}
