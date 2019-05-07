protected String getTrimmedToNullString(XPath xpath, String elementName, Node parentNode) throws XPathExpressionException {
    String str = (String) xpath.evaluate(elementName, parentNode, XPathConstants.STRING);
    if (str != null)
        str = str.trim();
    if (str != null && str.length() == 0)
        str = null;
    return str;
}
