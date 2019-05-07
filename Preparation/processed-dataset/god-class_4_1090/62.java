/**
	 * Returns the value of a boolean property. This method is thread-safe.
	 * @param name The property name
	 * @param def The default value
	 * @since jEdit 4.3pre17
	 */
public boolean getBooleanProperty(String name, boolean def) {
    Object obj = getProperty(name);
    return StandardUtilities.getBoolean(obj, def);
}
