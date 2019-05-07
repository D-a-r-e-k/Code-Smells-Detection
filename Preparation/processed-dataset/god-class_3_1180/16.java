/**
   * Get an output property that is in effect for the
   * transformation.  The property specified may be a property
   * that was set with setOutputProperty, or it may be a
   * property specified in the stylesheet.
   *
   * @param name A non-null String that specifies an output
   * property name, which may be namespace qualified.
   *
   * @return The string value of the output property, or null
   * if no property was found.
   *
   * @throws IllegalArgumentException If the property is not supported.
   *
   * @see javax.xml.transform.OutputKeys
   */
public String getOutputProperty(String name) throws IllegalArgumentException {
    String value = null;
    OutputProperties props = m_outputFormat;
    value = props.getProperty(name);
    if (null == value) {
        if (!OutputProperties.isLegalPropertyKey(name))
            throw new IllegalArgumentException(XSLMessages.createMessage(XSLTErrorResources.ER_OUTPUT_PROPERTY_NOT_RECOGNIZED, new Object[] { name }));
    }
    return value;
}
