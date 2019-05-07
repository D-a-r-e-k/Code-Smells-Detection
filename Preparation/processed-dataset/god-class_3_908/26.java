//}}} 
//{{{ getProperty() 
/**
     * Gets a jsXe global property. This returns a user defined value for the
     * property, the default value of the property if no user defined value is
     * found or the default value given if neither exist.
     * @param key The key of the property to get.
     * @param defaultValue The default value to return when the key is not found.
     * @return The value associated with the key or the default value if the key is not found.
     */
public static final String getProperty(String key, String defaultValue) {
    return props.getProperty(key, defaultProps.getProperty(key, defaultValue));
}
