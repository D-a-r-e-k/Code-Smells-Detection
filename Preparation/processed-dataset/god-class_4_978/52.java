/**
   * Returns the property with the specified name, formatting it with
   * the <code>java.text.MessageFormat.format()</code> method.
   * @param name The property
   * @param args The positional parameters
   */
public static final String getProperty(String name, Object[] args) {
    if (name == null)
        return null;
    if (args == null)
        return props.getProperty(name, name);
    else
        return MessageFormat.format(props.getProperty(name, name), args);
}
