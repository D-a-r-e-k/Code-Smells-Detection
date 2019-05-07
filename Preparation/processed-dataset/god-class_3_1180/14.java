/**
   * Get a copy of the output properties for the transformation.
   *
   * <p>The properties returned should contain properties set by the user,
   * and properties set by the stylesheet, and these properties
   * are "defaulted" by default properties specified by <a href="http://www.w3.org/TR/xslt#output">section 16 of the
   * XSL Transformations (XSLT) W3C Recommendation</a>.  The properties that
   * were specifically set by the user or the stylesheet should be in the base
   * Properties list, while the XSLT default properties that were not
   * specifically set should be the default Properties list.  Thus,
   * getOutputProperties().getProperty(String key) will obtain any
   * property in that was set by {@link #setOutputProperty},
   * {@link #setOutputProperties}, in the stylesheet, <em>or</em> the default
   * properties, while
   * getOutputProperties().get(String key) will only retrieve properties
   * that were explicitly set by {@link #setOutputProperty},
   * {@link #setOutputProperties}, or in the stylesheet.</p>
   *
   * <p>Note that mutation of the Properties object returned will not
   * effect the properties that the transformation contains.</p>
   *
   * <p>If any of the argument keys are not recognized and are not
   * namespace qualified, the property will be ignored.  In other words the
   * behaviour is not orthogonal with setOutputProperties.</p>
   *
   * @return A copy of the set of output properties in effect
   * for the next transformation.
   *
   * @see javax.xml.transform.OutputKeys
   * @see java.util.Properties
   */
public Properties getOutputProperties() {
    return (Properties) m_outputFormat.getProperties().clone();
}
