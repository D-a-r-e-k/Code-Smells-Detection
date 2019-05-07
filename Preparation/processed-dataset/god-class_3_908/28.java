//}}} 
//{{{ setIntegerProperty() method 
/**
     * Sets the value of an integer property.
     * @param name The property
     * @param value The value
     * @since jsXe 0.2 pre24
     */
public static final void setIntegerProperty(String name, int value) {
    setProperty(name, String.valueOf(value));
}
