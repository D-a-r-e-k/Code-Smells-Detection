//}}}  
//{{{ getPatternProperty()  
/**
	 * Returns the value of a property as a regular expression.
	 * This method is thread-safe.
	 * @param name The property name
	 * @param flags Regular expression compilation flags
	 * @since jEdit 4.3pre5
	 */
public Pattern getPatternProperty(String name, int flags) {
    synchronized (propertyLock) {
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
            return null;
        else if (obj instanceof Pattern)
            return (Pattern) obj;
        else {
            Pattern re = Pattern.compile(obj.toString(), flags);
            properties.put(name, new PropValue(re, defaultValueFlag));
            return re;
        }
    }
}
