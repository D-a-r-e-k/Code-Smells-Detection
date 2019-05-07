//}}}  
//{{{ setProperty() method  
/**
	 * Sets the value of a buffer-local property.
	 * @param name The property name
	 * @param value The property value
	 * @since jEdit 4.0pre1
	 */
public void setProperty(String name, Object value) {
    if (value == null)
        properties.remove(name);
    else {
        PropValue test = properties.get(name);
        if (test == null)
            properties.put(name, new PropValue(value, false));
        else if (test.value.equals(value)) {
        } else {
            test.value = value;
            test.defaultValue = false;
        }
    }
}
