/*
     * Gets the state of the feature of the scanner.
     */
public boolean getFeature(String featureId) throws XMLConfigurationException {
    if (VALIDATION.equals(featureId)) {
        return fValidation;
    } else if (NOTIFY_CHAR_REFS.equals(featureId)) {
        return fNotifyCharRefs;
    }
    throw new XMLConfigurationException(XMLConfigurationException.NOT_RECOGNIZED, featureId);
}
