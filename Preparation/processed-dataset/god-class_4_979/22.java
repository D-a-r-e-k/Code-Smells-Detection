protected Boolean getBoolean(XPath xpath, String elementName, Document document) throws XPathExpressionException {
    Node directive = (Node) xpath.evaluate(elementName, document, XPathConstants.NODE);
    if (directive == null || directive.getTextContent() == null)
        return null;
    String val = directive.getTextContent();
    if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("yes") || val.equalsIgnoreCase("y"))
        return Boolean.TRUE;
    return Boolean.FALSE;
}
