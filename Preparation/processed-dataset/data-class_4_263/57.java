//}}}  
//{{{ unsetProperty() method  
/**
	 * Clears the value of a buffer-local property.
	 * @param name The property name
	 * @since jEdit 4.0pre1
	 */
public void unsetProperty(String name) {
    properties.remove(name);
}
