/**
   * If we store properties, we need to read them, too !
   * @param name The name of the property to read
   * @return The value of the specified property
   */
public static String getProperty(String name) {
    return props.getProperty(name);
}
