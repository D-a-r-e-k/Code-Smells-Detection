//}}}  
//{{{ setBooleanProperty() method  
/**
	 * Sets a boolean property.
	 * @param name The property name
	 * @param value The value
	 * @since jEdit 4.0pre1
	 */
public void setBooleanProperty(String name, boolean value) {
    setProperty(name, value ? Boolean.TRUE : Boolean.FALSE);
}
