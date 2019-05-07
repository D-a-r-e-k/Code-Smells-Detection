// setFeature(String,boolean)  
/**
     * Returns a list of property identifiers that are recognized by
     * this component. This method may return null if no properties
     * are recognized by this component.
     */
public String[] getRecognizedProperties() {
    return (String[]) (RECOGNIZED_PROPERTIES.clone());
}
