//}}} 
//{{{ setProperty() 
/**
     * Sets a global property to jsXe.
     * @param key The key name for the property.
     * @param value The value to associate with the key.
     * @return The previous value for the key, or null if there was none.
     */
public static Object setProperty(String key, String value) {
    if (value == null) {
        props.remove(key);
        return null;
    } else {
        return props.setProperty(key, value);
    }
}
