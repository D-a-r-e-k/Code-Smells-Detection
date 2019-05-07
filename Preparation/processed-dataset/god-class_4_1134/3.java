// setProperty(String,Object)  
/*
     * Sets the feature of the scanner.
     */
public void setFeature(String featureId, boolean value) throws XMLConfigurationException {
    if (VALIDATION.equals(featureId)) {
        fValidation = value;
    } else if (NOTIFY_CHAR_REFS.equals(featureId)) {
        fNotifyCharRefs = value;
    }
}
