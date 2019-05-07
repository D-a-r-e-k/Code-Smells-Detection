//}}}  
//{{{ getIntegerProperty() method  
/**
	 * Returns the value of an integer property. This method is thread-safe.
	 * @param name The property name
	 * @since jEdit 4.0pre1
	 */
public int getIntegerProperty(String name, int defaultValue) {
    boolean defaultValueFlag;
    Object obj;
    PropValue value = properties.get(name);
    if (value != null) {
        obj = value.value;
        defaultValueFlag = value.defaultValue;
    } else {
        obj = getProperty(name);
        // will be cached from now on...  
        defaultValueFlag = true;
    }
    if (obj == null)
        return defaultValue;
    else if (obj instanceof Number)
        return ((Number) obj).intValue();
    else {
        try {
            int returnValue = Integer.parseInt(obj.toString().trim());
            properties.put(name, new PropValue(returnValue, defaultValueFlag));
            return returnValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
