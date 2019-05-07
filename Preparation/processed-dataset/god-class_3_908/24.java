//}}} 
//{{{ getDefaultProperty() 
/**
     * Gets a default global property. Returns null if there is no default
     * property for the given key.
     * 
     */
public static final String getDefaultProperty(String key) {
    return defaultProps.getProperty(key);
}
