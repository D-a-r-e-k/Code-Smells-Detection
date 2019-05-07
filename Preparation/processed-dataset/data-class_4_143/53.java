/**
   * Unsets (clears) a property.
   * @param name The property
   */
public static void unsetProperty(String name) {
    if (defaultProps.get(name) != null)
        props.put(name, "");
    else
        props.remove(name);
}
