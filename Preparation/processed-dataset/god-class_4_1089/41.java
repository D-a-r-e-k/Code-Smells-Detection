//}}}  
//}}}  
//{{{ Deprecated methods  
//{{{ putProperty() method  
/**
	 * @deprecated Call <code>setProperty()</code> instead.
	 */
@Deprecated
public void putProperty(Object name, Object value) {
    // for backwards compatibility  
    if (!(name instanceof String))
        return;
    setProperty((String) name, value);
}
