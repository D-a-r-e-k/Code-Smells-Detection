/**
   * Add a parameter for the transformation.
   *
   * <p>Pass a qualified name as a two-part string, the namespace URI
   * enclosed in curly braces ({}), followed by the local name. If the
   * name has a null URL, the String only contain the local name. An
   * application can safely check for a non-null URI by testing to see if the first
   * character of the name is a '{' character.</p>
   * <p>For example, if a URI and local name were obtained from an element
   * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
   * then the qualified name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
   * no prefix is used.</p>
   *
   * @param name The name of the parameter, which may begin with a namespace URI
   * in curly braces ({}).
   * @param value The value object.  This can be any valid Java object. It is
   * up to the processor to provide the proper object coersion or to simply
   * pass the object on for use in an extension.
   */
public void setParameter(String name, Object value) {
    if (value == null) {
        throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_INVALID_SET_PARAM_VALUE, new Object[] { name }));
    }
    if (null == m_params) {
        m_params = new Hashtable();
    }
    m_params.put(name, value);
}
