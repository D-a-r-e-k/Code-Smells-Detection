// getFeatureDefault(String):Boolean 
/** Returns the default state for a property. */
public Object getPropertyDefault(String propertyId) {
    int length = RECOGNIZED_PROPERTIES != null ? RECOGNIZED_PROPERTIES.length : 0;
    for (int i = 0; i < length; i++) {
        if (RECOGNIZED_PROPERTIES[i].equals(propertyId)) {
            return RECOGNIZED_PROPERTIES_DEFAULTS[i];
        }
    }
    return null;
}
