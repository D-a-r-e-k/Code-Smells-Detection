/**
   * Set an output property that will be in effect for the
   * transformation.
   *
   * <p>Pass a qualified property name as a two-part string, the namespace URI
   * enclosed in curly braces ({}), followed by the local name. If the
   * name has a null URL, the String only contain the local name. An
   * application can safely check for a non-null URI by testing to see if the first
   * character of the name is a '{' character.</p>
   * <p>For example, if a URI and local name were obtained from an element
   * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
   * then the qualified name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
   * no prefix is used.</p>
   *
   * <p>The Properties object that was passed to {@link #setOutputProperties} won't
   * be effected by calling this method.</p>
   *
   * @param name A non-null String that specifies an output
   * property name, which may be namespace qualified.
   * @param value The non-null string value of the output property.
   *
   * @throws IllegalArgumentException If the property is not supported, and is
   * not qualified with a namespace.
   *
   * @see javax.xml.transform.OutputKeys
   */
public void setOutputProperty(String name, String value) throws IllegalArgumentException {
    if (!OutputProperties.isLegalPropertyKey(name))
        throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_OUTPUT_PROPERTY_NOT_RECOGNIZED, new Object[] { name }));
    //"output property not recognized: "  
    //+ name);  
    m_outputFormat.setProperty(name, value);
}
