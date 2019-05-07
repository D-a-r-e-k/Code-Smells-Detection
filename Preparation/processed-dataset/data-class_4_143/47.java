/**
   * Set a property.
   * @param name Property's name
   * @param value The value to store as <code>name</code>
   */
public static void setProperty(String name, String value) {
    if (name == null || value == null)
        return;
    props.put(name, value);
}
