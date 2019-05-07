/**
     * Reads a double attribute.
     */
private double toDouble(IXMLElement elem, String value, double defaultValue, double min, double max) throws IOException {
    try {
        double d = Double.valueOf(value);
        return Math.max(Math.min(d, max), min);
    } catch (NumberFormatException e) {
        return defaultValue;
    }
}
