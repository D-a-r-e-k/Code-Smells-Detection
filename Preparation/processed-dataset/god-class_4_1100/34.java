/**
     * Returns a value as a length.
     * http://www.w3.org/TR/SVGMobile12/types.html#DataTypeLength
     */
private double toLength(IXMLElement elem, String str, double percentFactor) throws IOException {
    double scaleFactor = 1d;
    if (str == null || str.length() == 0 || str.equals("none")) {
        return 0d;
    }
    if (str.endsWith("%")) {
        str = str.substring(0, str.length() - 1);
        scaleFactor = percentFactor;
    } else if (str.endsWith("px")) {
        str = str.substring(0, str.length() - 2);
    } else if (str.endsWith("pt")) {
        str = str.substring(0, str.length() - 2);
        scaleFactor = 1.25;
    } else if (str.endsWith("pc")) {
        str = str.substring(0, str.length() - 2);
        scaleFactor = 15;
    } else if (str.endsWith("mm")) {
        str = str.substring(0, str.length() - 2);
        scaleFactor = 3.543307;
    } else if (str.endsWith("cm")) {
        str = str.substring(0, str.length() - 2);
        scaleFactor = 35.43307;
    } else if (str.endsWith("in")) {
        str = str.substring(0, str.length() - 2);
        scaleFactor = 90;
    } else if (str.endsWith("em")) {
        str = str.substring(0, str.length() - 2);
        // XXX - This doesn't work 
        scaleFactor = toLength(elem, readAttribute(elem, "font-size", "0"), percentFactor);
    } else {
        scaleFactor = 1d;
    }
    return Double.parseDouble(str) * scaleFactor;
}
