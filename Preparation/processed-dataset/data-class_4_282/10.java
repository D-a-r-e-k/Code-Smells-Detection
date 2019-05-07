public void configurePreview() {
    if (needCopyToUI)
        copyConfigurationToUI();
    transparentBackground = false;
    smoothing = 1.0;
    adaptive = hideBackfaces = true;
    generateHDR = false;
    surfaceError = 0.02;
    shadingMode = HYBRID;
    samplesPerPixel = subsample = 1;
}
