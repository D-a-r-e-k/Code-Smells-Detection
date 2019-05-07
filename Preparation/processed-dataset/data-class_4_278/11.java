public void setConfiguration(String property, Object value) {
    needCopyToUI = true;
    if ("maxRayDepth".equals(property))
        maxRayDepth = (Integer) value;
    else if ("minRayIntensity".equals(property))
        minRayIntensity = ((Number) value).floatValue();
    else if ("materialStepSize".equals(property))
        stepSize = ((Number) value).doubleValue();
    else if ("textureSmoothing".equals(property))
        smoothing = ((Number) value).doubleValue();
    else if ("extraGISmoothing".equals(property))
        extraGISmoothing = ((Number) value).doubleValue();
    else if ("extraGIEnvSmoothing".equals(property))
        extraGIEnvSmoothing = ((Number) value).doubleValue();
    else if ("reduceAccuracyForDistant".equals(property))
        adaptive = (Boolean) value;
    else if ("russianRouletteSampling".equals(property))
        roulette = (Boolean) value;
    else if ("useLessMemory".equals(property))
        reducedMemory = (Boolean) value;
    else if ("maxSurfaceError".equals(property))
        surfaceError = ((Number) value).doubleValue();
    else if ("antialiasing".equals(property))
        antialiasLevel = (Integer) value;
    else if ("depthOfField".equals(property))
        depth = (Boolean) value;
    else if ("gloss".equals(property))
        gloss = (Boolean) value;
    else if ("softShadows".equals(property))
        penumbra = (Boolean) value;
    else if ("minRaysPerPixel".equals(property))
        minRays = (Integer) value;
    else if ("maxRaysPerPixel".equals(property))
        maxRays = (Integer) value;
    else if ("transparentBackground".equals(property))
        transparentBackground = (Boolean) value;
    else if ("highDynamicRange".equals(property))
        generateHDR = (Boolean) value;
    else if ("globalIlluminationMode".equals(property))
        giMode = (Integer) value;
    else if ("raysToSampleEnvironment".equals(property))
        diffuseRays = (Integer) value;
    else if ("globalIlluminationPhotons".equals(property))
        globalPhotons = (Integer) value;
    else if ("globalIlluminationPhotonsInEstimate".equals(property))
        globalNeighborPhotons = (Integer) value;
    else if ("caustics".equals(property))
        caustics = (Boolean) value;
    else if ("causticsPhotons".equals(property))
        causticsPhotons = (Integer) value;
    else if ("causticsPhotonsInEstimate".equals(property))
        causticsNeighborPhotons = (Integer) value;
    else if ("scatteringMode".equals(property))
        scatterMode = (Integer) value;
    else if ("scatteringPhotons".equals(property))
        volumePhotons = (Integer) value;
    else if ("scatteringPhotonsInEstimate".equals(property))
        volumeNeighborPhotons = (Integer) value;
}
