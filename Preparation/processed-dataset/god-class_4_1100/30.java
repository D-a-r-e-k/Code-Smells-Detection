/**
     * Reads an attribute that is not inherited, unless its value is "inherit".
     */
private String readAttribute(IXMLElement elem, String attributeName, String defaultValue) {
    if (elem.hasAttribute(attributeName, SVG_NAMESPACE)) {
        String value = elem.getAttribute(attributeName, SVG_NAMESPACE, null);
        if (value.equals("inherit")) {
            return readAttribute(elem.getParent(), attributeName, defaultValue);
        } else {
            return value;
        }
    } else if (elem.hasAttribute(attributeName)) {
        String value = elem.getAttribute(attributeName, null);
        if (value.equals("inherit")) {
            return readAttribute(elem.getParent(), attributeName, defaultValue);
        } else {
            return value;
        }
    } else {
        return defaultValue;
    }
}
