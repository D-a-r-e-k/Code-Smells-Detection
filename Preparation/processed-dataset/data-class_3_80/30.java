//}}} 
//{{{ setBooleanProperty() 
/**
     * Sets the value of an boolean property.
     * @param name The property
     * @param value The value
     * @since jsXe 0.4 pre3
     */
public static final void setBooleanProperty(String name, boolean value) {
    setProperty(name, String.valueOf(value));
}
