public Map<String, Object> getConfiguration() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("textureSmoothing", smoothing);
    map.put("reduceAccuracyForDistant", adaptive);
    map.put("hideBackfaces", hideBackfaces);
    map.put("highDynamicRange", generateHDR);
    map.put("maxSurfaceError", surfaceError);
    map.put("shadingMethod", shadingMode);
    map.put("transparentBackground", transparentBackground);
    int antialiasLevel = 0;
    if (samplesPerPixel == 2)
        antialiasLevel = subsample;
    else if (samplesPerPixel == 3)
        antialiasLevel = (subsample == 1 ? 3 : 4);
    map.put("antialiasing", antialiasLevel);
    return map;
}
