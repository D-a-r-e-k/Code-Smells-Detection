/**
     * Reads an attribute that is inherited.
     */
private String readInheritAttribute(IXMLElement elem, String attributeName, String defaultValue) {
    if (elem.hasAttribute(attributeName, SVG_NAMESPACE)) {
        String value = elem.getAttribute(attributeName, SVG_NAMESPACE, null);
        if (value.equals("inherit")) {
            return readInheritAttribute(elem.getParent(), attributeName, defaultValue);
        } else {
            return value;
        }
    } else if (elem.hasAttribute(attributeName)) {
        String value = elem.getAttribute(attributeName, "");
        if (value.equals("inherit")) {
            return readInheritAttribute(elem.getParent(), attributeName, defaultValue);
        } else {
            return value;
        }
    } else if (elem.getParent() != null && (elem.getParent().getNamespace() == null || elem.getParent().getNamespace().equals(SVG_NAMESPACE))) {
        return readInheritAttribute(elem.getParent(), attributeName, defaultValue);
    } else {
        return defaultValue;
    }
}
