/** Copy the current configuration to the user interface. */
private void copyConfigurationToUI() {
    needCopyToUI = false;
    if (configPanel == null)
        getConfigPanel();
    smoothField.setValue(smoothing);
    adaptiveBox.setState(adaptive);
    hideBackfaceBox.setState(hideBackfaces);
    hdrBox.setState(generateHDR);
    errorField.setValue(surfaceError);
    shadeChoice.setSelectedIndex(shadingMode);
    transparentBox.setState(transparentBackground);
    if (samplesPerPixel == 1) {
        aliasChoice.setSelectedIndex(0);
    } else if (subsample == 1) {
        aliasChoice.setSelectedIndex(2);
        sampleChoice.setSelectedIndex(samplesPerPixel - 2);
    } else {
        aliasChoice.setSelectedIndex(1);
        sampleChoice.setSelectedIndex(samplesPerPixel - 2);
    }
    sampleChoice.setEnabled(aliasChoice.getSelectedIndex() > 0);
}
