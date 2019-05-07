// reset(XMLComponentManager) 
/** Sets a feature. */
public void setFeature(String featureId, boolean state) throws XMLConfigurationException {
    if (featureId.equals(AUGMENTATIONS)) {
        fAugmentations = state;
        return;
    }
    if (featureId.equals(REPORT_ERRORS)) {
        fReportErrors = state;
        return;
    }
    if (featureId.equals(IGNORE_OUTSIDE_CONTENT)) {
        fIgnoreOutsideContent = state;
        return;
    }
}
