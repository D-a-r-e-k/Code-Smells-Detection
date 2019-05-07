/**
   * Set the output properties for the transformation.  These
   * properties will override properties set in the Templates
   * with xsl:output.
   *
   * <p>If argument to this function is null, any properties
   * previously set are removed, and the value will revert to the value
   * defined in the templates object.</p>
   *
   * <p>Pass a qualified property key name as a two-part string, the namespace URI
   * enclosed in curly braces ({}), followed by the local name. If the
   * name has a null URL, the String only contain the local name. An
   * application can safely check for a non-null URI by testing to see if the first
   * character of the name is a '{' character.</p>
   * <p>For example, if a URI and local name were obtained from an element
   * defined with &lt;xyz:foo xmlns:xyz="http://xyz.foo.com/yada/baz.html"/&gt;,
   * then the qualified name would be "{http://xyz.foo.com/yada/baz.html}foo". Note that
   * no prefix is used.</p>
   *
   * @param oformat A set of output properties that will be
   * used to override any of the same properties in affect
   * for the transformation.
   *
   * @see javax.xml.transform.OutputKeys
   * @see java.util.Properties
   *
   * @throws IllegalArgumentException if any of the argument keys are not
   * recognized and are not namespace qualified.
   */
public void setOutputProperties(Properties oformat) throws IllegalArgumentException {
    if (null != oformat) {
        // See if an *explicit* method was set.  
        String method = (String) oformat.get(OutputKeys.METHOD);
        if (null != method)
            m_outputFormat = new OutputProperties(method);
        else
            m_outputFormat = new OutputProperties();
        m_outputFormat.copyFrom(oformat);
    } else {
        // if oformat is null JAXP says that any props previously set are removed  
        // and we are to revert back to those in the templates object (i.e. Stylesheet).  
        m_outputFormat = null;
    }
}
