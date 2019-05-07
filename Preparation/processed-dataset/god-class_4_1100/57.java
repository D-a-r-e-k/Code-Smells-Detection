/**
     * Reads a double attribute.
     */
private double toDouble(IXMLElement elem, String value) throws IOException {
    return toDouble(elem, value, 0, Double.MIN_VALUE, Double.MAX_VALUE);
}
