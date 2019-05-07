/**
     * Reads a font size attribute that is inherited.
     * As specified by
     * http://www.w3.org/TR/SVGMobile12/text.html#FontPropertiesUsedBySVG
     * http://www.w3.org/TR/2006/CR-xsl11-20060220/#font-getChildCount
     */
private double readInheritFontSizeAttribute(IXMLElement elem, String attributeName, String defaultValue) throws IOException {
    String value = null;
    if (elem.hasAttribute(attributeName, SVG_NAMESPACE)) {
        value = elem.getAttribute(attributeName, SVG_NAMESPACE, null);
    } else if (elem.hasAttribute(attributeName)) {
        value = elem.getAttribute(attributeName, null);
    } else if (elem.getParent() != null && (elem.getParent().getNamespace() == null || elem.getParent().getNamespace().equals(SVG_NAMESPACE))) {
        return readInheritFontSizeAttribute(elem.getParent(), attributeName, defaultValue);
    } else {
        value = defaultValue;
    }
    if (value.equals("inherit")) {
        return readInheritFontSizeAttribute(elem.getParent(), attributeName, defaultValue);
    } else if (SVG_ABSOLUTE_FONT_SIZES.containsKey(value)) {
        return SVG_ABSOLUTE_FONT_SIZES.get(value);
    } else if (SVG_RELATIVE_FONT_SIZES.containsKey(value)) {
        return SVG_RELATIVE_FONT_SIZES.get(value) * readInheritFontSizeAttribute(elem.getParent(), attributeName, defaultValue);
    } else if (value.endsWith("%")) {
        double factor = Double.valueOf(value.substring(0, value.length() - 1));
        return factor * readInheritFontSizeAttribute(elem.getParent(), attributeName, defaultValue);
    } else {
        //return toScaledNumber(elem, value); 
        return toNumber(elem, value);
    }
}
