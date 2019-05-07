//}}} 
//{{{ getProperty() 
/**
     * Gets a jsXe global property. This returns a user defined value for the
     * property, the default value of the property if no user defined value is
     * found or null if neither exist.
     * @param key The key of the property to get.
     * @return The value associated with the key or null if the key is not found.
     */
public static final String getProperty(String key) {
    return getProperty(key, null);
}
