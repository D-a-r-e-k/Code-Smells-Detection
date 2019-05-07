// getCharacterOffset():int 
// 
// HTMLComponent methods 
// 
/** Returns the default state for a feature. */
public Boolean getFeatureDefault(String featureId) {
    int length = RECOGNIZED_FEATURES != null ? RECOGNIZED_FEATURES.length : 0;
    for (int i = 0; i < length; i++) {
        if (RECOGNIZED_FEATURES[i].equals(featureId)) {
            return RECOGNIZED_FEATURES_DEFAULTS[i];
        }
    }
    return null;
}
