/** Copy the current configuration to the user interface. */
protected void copyConfigurationToUI() {
    needCopyToUI = false;
    if (configPanel == null)
        getConfigPanel();
    rayDepthField.setValue(maxRayDepth);
    rayCutoffField.setValue(minRayIntensity);
    stepSizeField.setValue(stepSize);
    smoothField.setValue(smoothing);
    extraGIField.setValue(extraGISmoothing);
    extraGIEnvField.setValue(extraGIEnvSmoothing);
    adaptiveBox.setState(adaptive);
    rouletteBox.setState(roulette);
    errorField.setValue(surfaceError);
    aliasChoice.setSelectedIndex(antialiasLevel);
    depthBox.setState(depth);
    glossBox.setState(gloss);
    shadowBox.setState(penumbra);
    minRaysChoice.setSelectedValue(Integer.toString(minRays));
    maxRaysChoice.setSelectedValue(Integer.toString(maxRays));
    reducedMemoryBox.setState(reducedMemory);
    giModeChoice.setSelectedIndex(giMode);
    diffuseRaysChoice.setSelectedValue(Integer.toString(diffuseRays));
    globalPhotonsField.setValue(globalPhotons);
    globalNeighborPhotonsField.setValue(globalNeighborPhotons);
    causticsBox.setState(caustics);
    causticsPhotonsField.setValue(causticsPhotons);
    causticsNeighborPhotonsField.setValue(causticsNeighborPhotons);
    scatterModeChoice.setSelectedIndex(scatterMode);
    volumePhotonsField.setValue(volumePhotons);
    volumeNeighborPhotonsField.setValue(volumeNeighborPhotons);
    transparentBox.setState(transparentBackground);
    hdrBox.setState(generateHDR);
    // Generate events to force appropriate components to be enabled or disabled. 
    aliasChoice.dispatchEvent(new ValueChangedEvent(aliasChoice));
}
