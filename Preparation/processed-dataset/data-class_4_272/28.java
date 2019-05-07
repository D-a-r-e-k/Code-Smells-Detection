/**
     * Reads a color attribute that is inherited.
     * This is similar to {@code readInheritAttribute}, but takes care of the
     * "currentColor" magic attribute value.
     */
private String readInheritColorAttribute(IXMLElement elem, String attributeName, String defaultValue) {
    String value = null;
    if (elem.hasAttribute(attributeName, SVG_NAMESPACE)) {
        value = elem.getAttribute(attributeName, SVG_NAMESPACE, null);
        if (value.equals("inherit")) {
            return readInheritColorAttribute(elem.getParent(), attributeName, defaultValue);
        }
    } else if (elem.hasAttribute(attributeName)) {
        value = elem.getAttribute(attributeName, "");
        if (value.equals("inherit")) {
            return readInheritColorAttribute(elem.getParent(), attributeName, defaultValue);
        }
    } else if (elem.getParent() != null && (elem.getParent().getNamespace() == null || elem.getParent().getNamespace().equals(SVG_NAMESPACE))) {
        value = readInheritColorAttribute(elem.getParent(), attributeName, defaultValue);
    } else {
        value = defaultValue;
    }
    if (value != null && value.toLowerCase().equals("currentcolor") && !attributeName.equals("color")) {
        // Lets do some magic stuff for "currentColor" attribute value 
        value = readInheritColorAttribute(elem, "color", "defaultValue");
    }
    return value;
}
