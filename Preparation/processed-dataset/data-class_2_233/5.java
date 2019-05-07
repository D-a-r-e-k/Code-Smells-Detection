/**
     * Create a suitable XPath-like context from an element name and optional
     * attribute name. 
     * 
     * @param element
     * @param attribute
     * @return CharSequence context
     */
public static CharSequence elementContext(CharSequence element, CharSequence attribute) {
    return attribute == null ? "" : element + "/@" + attribute;
}
