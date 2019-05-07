//}}} 
//{{{ getIntegerProperty() 
/**
     * Returns the value of an integer property.
     * @param name The property
     * @param def The default value
     * @since jsXe 0.2 pre24
     */
public static final int getIntegerProperty(String key, int defaultValue) {
    int intValue = defaultValue;
    String value = getProperty(key);
    if (value == null) {
        return defaultValue;
    } else {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException nf) {
            return defaultValue;
        }
    }
}
