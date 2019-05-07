//}}} 
//{{{ getBooleanProperty() method 
/**
     * Gets the value of an boolean property.
     * @param name The property
     * @param defaultValue The default value of the property
     * @since jsXe 0.4 pre3
     */
public static final boolean getBooleanProperty(String name, boolean defaultValue) {
    boolean booleanValue = defaultValue;
    String value = getProperty(name);
    if (value == null) {
        return defaultValue;
    } else {
        return Boolean.valueOf(value).booleanValue();
    }
}
