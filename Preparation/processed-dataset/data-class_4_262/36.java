//}}}  
//{{{ getDefaultProperty() method  
@Override
public Object getDefaultProperty(String name) {
    Object retVal;
    if (mode != null) {
        retVal = mode.getProperty(name);
        if (retVal == null)
            return null;
        setDefaultProperty(name, retVal);
        return retVal;
    }
    // Now try buffer.<property>  
    String value = jEdit.getProperty("buffer." + name);
    if (value == null)
        return null;
    // Try returning it as an integer first  
    try {
        retVal = new Integer(value);
    } catch (NumberFormatException nf) {
        retVal = value;
    }
    return retVal;
}
