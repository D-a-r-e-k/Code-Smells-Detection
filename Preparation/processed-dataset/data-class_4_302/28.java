// reset(XMLComponentManager)  
/**
     * Returns a list of feature identifiers that are recognized by
     * this component. This method may return null if no features
     * are recognized by this component.
     */
public String[] getRecognizedFeatures() {
    return (String[]) (RECOGNIZED_FEATURES.clone());
}
