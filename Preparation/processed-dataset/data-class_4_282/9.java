public void setConfiguration(String property, Object value) {
    needCopyToUI = true;
    if ("textureSmoothing".equals(property))
        smoothing = ((Number) value).doubleValue();
    else if ("reduceAccuracyForDistant".equals(property))
        adaptive = (Boolean) value;
    else if ("hideBackfaces".equals(property))
        hideBackfaces = (Boolean) value;
    else if ("highDynamicRange".equals(property))
        generateHDR = (Boolean) value;
    else if ("maxSurfaceError".equals(property))
        surfaceError = ((Number) value).doubleValue();
    else if ("shadingMethod".equals(property))
        shadingMode = (Integer) value;
    else if ("transparentBackground".equals(property))
        transparentBackground = (Boolean) value;
    else if ("antialiasing".equals(property)) {
        int antialiasLevel = (Integer) value;
        switch(antialiasLevel) {
            case 0:
                samplesPerPixel = subsample = 1;
                break;
            case 1:
                samplesPerPixel = 2;
                subsample = 1;
                break;
            case 2:
                samplesPerPixel = 2;
                subsample = 2;
                break;
            case 3:
                samplesPerPixel = 3;
                subsample = 1;
                break;
            case 4:
                samplesPerPixel = 3;
                subsample = 3;
                break;
        }
    }
}
