public boolean recordConfiguration() {
    smoothing = smoothField.getValue();
    adaptive = adaptiveBox.getState();
    hideBackfaces = hideBackfaceBox.getState();
    generateHDR = hdrBox.getState();
    surfaceError = errorField.getValue();
    shadingMode = shadeChoice.getSelectedIndex();
    transparentBackground = transparentBox.getState();
    if (aliasChoice.getSelectedIndex() == 0)
        samplesPerPixel = subsample = 1;
    else if (aliasChoice.getSelectedIndex() == 1)
        samplesPerPixel = subsample = sampleChoice.getSelectedIndex() + 2;
    else {
        samplesPerPixel = sampleChoice.getSelectedIndex() + 2;
        subsample = 1;
    }
    return true;
}
