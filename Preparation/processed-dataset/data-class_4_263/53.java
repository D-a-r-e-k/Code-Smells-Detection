//}}}  
//{{{ getProperty() method  
/**
	 * Returns the value of a buffer-local property.<p>
	 *
	 * Using this method is generally discouraged, because it returns an
	 * <code>Object</code> which must be cast to another type
	 * in order to be useful, and this can cause problems if the object
	 * is of a different type than what the caller expects.<p>
	 *
	 * The following methods should be used instead:
	 * <ul>
	 * <li>{@link #getStringProperty(String)}</li>
	 * <li>{@link #getBooleanProperty(String)}</li>
	 * <li>{@link #getIntegerProperty(String,int)}</li>
	 * </ul>
	 *
	 * This method is thread-safe.
	 *
	 * @param name The property name. For backwards compatibility, this
	 * is an <code>Object</code>, not a <code>String</code>.
	 */
public Object getProperty(Object name) {
    synchronized (propertyLock) {
        // First try the buffer-local properties  
        PropValue o = properties.get(name);
        if (o != null)
            return o.value;
        // For backwards compatibility  
        if (!(name instanceof String))
            return null;
        Object retVal = getDefaultProperty((String) name);
        if (retVal == null)
            return null;
        else {
            properties.put(name, new PropValue(retVal, true));
            return retVal;
        }
    }
}
