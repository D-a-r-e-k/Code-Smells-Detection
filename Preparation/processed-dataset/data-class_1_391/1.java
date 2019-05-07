public Object createObject(Attributes attributes) throws Exception {
    JRGenericPrintElement element = (JRGenericPrintElement) digester.peek();
    String name = attributes.getValue(JRXmlConstants.ATTRIBUTE_name);
    return new Parameter(element, name);
}
