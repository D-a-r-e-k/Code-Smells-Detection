/**
     * Reads a text attribute.
     * This method takes the "xml:space" attribute into account.
     * http://www.w3.org/TR/SVGMobile12/text.html#WhiteSpace
     */
private String toText(IXMLElement elem, String value) throws IOException {
    String space = readInheritAttribute(elem, "xml:space", "default");
    if (space.equals("default")) {
        return value.trim().replaceAll("\\s++", " ");
    } else /*if (space.equals("preserve"))*/
    {
        return value;
    }
}
