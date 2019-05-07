public void configurePreview() {
    if (needCopyToUI)
        copyConfigurationToUI();
    maxRayDepth = 6;
    minRayIntensity = 0.02f;
    antialiasLevel = 0;
    depth = gloss = penumbra = transparentBackground = generateHDR = false;
    minRays = maxRays = 1;
    stepSize = 1.0;
    smoothing = 1.0;
    extraGISmoothing = 10.0;
    extraGIEnvSmoothing = 100.0;
    adaptive = true;
    reducedMemory = false;
    roulette = false;
    surfaceError = 0.02;
    giMode = GI_NONE;
    scatterMode = SCATTER_SINGLE;
    caustics = false;
}
