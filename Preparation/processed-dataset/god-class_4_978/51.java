/**
   * Fetches a property, returning the default value if it's not
   * defined.
   * @param name The property
   * @param def The default value
   */
public static final String getProperty(String name, String def) {
    return props.getProperty(name, def);
}
