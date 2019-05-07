//}}}  
//{{{ getBooleanProperty() methods  
/**
	 * Returns the value of a boolean property. This method is thread-safe.
	 * @param name The property name
	 * @since jEdit 4.0pre1
	 */
public boolean getBooleanProperty(String name) {
    return getBooleanProperty(name, false);
}
