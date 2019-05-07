//}}}  
//{{{ setDefaultProperty() method  
public void setDefaultProperty(String name, Object value) {
    properties.put(name, new PropValue(value, true));
}
