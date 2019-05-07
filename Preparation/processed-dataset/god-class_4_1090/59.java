//}}}  
//{{{ getStringProperty() method  
/**
	 * Returns the value of a string property. This method is thread-safe.
	 * @param name The property name
	 * @since jEdit 4.0pre1
	 */
public String getStringProperty(String name) {
    Object obj = getProperty(name);
    if (obj != null)
        return obj.toString();
    else
        return null;
}
