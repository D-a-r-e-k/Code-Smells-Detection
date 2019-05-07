/******************************************************************************/
/**
 * Loads the actual desired values from the {@link #config gpConfiguration} to
 * the fields, where they are used later.
 * 
 * @param valueID {@link #VALUES_PUR} for a normal run or {@link #VALUES_INC}
 * for a layout update process.
 */
protected void loadRuntimeValues(int valueID) {
    maxRounds = applyCellList.size() * 4;
    //estimated value; reached rarely 
    countRounds = 0;
    //start value; counts the rounds in calculate() 
    isActive = isTrue((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_ENABLED));
    recursionDepth = Integer.parseInt((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_DEPTH));
    layoutUpdateMethod = (String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_METHOD);
    if (valueID == VALUES_PUR) {
        initTemperature = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_INIT_TEMPERATURE));
        minTemperature = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_MIN_TEMPERATURE));
        maxTemperature = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_MAX_TEMPERATURE));
        prefEdgeLength = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_PREF_EDGE_LENGTH));
        gravitation = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_GRAVITATION));
        randomImpulseRange = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_RANDOM_IMPULSE_RANGE));
        overlapDetectWidth = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_OVERLAPPING_DETECTION_WIDTH));
        overlapPrefDistance = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_OVERLAPPING_PREF_DISTANCE));
        shouldEndPerAverage = isTrue((String) config.get(GEMLayoutSettings.KEY_COMPUTE_PERMUTATION));
        shouldComputePermutation = isTrue((String) config.get(GEMLayoutSettings.KEY_END_CONDITION_AVERAGE));
        avoidOverlapping = isTrue((String) config.get(GEMLayoutSettings.KEY_AVOID_OVERLAPPING));
        alphaOsc = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_ALPHA_OSC));
        alphaRot = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_ALPHA_ROT));
        sigmaOsc = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_SIGMA_OSC));
        sigmaRot = Double.parseDouble((String) config.get(//gets 1/x 
        GEMLayoutSettings.KEY_SIGMA_ROT));
        useOptimizeAlgorithm = isTrue((String) config.get(GEMLayoutSettings.KEY_OPTIMIZE_ALGORITHM_ENABLED));
        optimizationAlgorithmConfig = (Properties) config.get(GEMLayoutSettings.KEY_OPTIMIZE_ALGORITHM_CONFIG);
    } else if (valueID == VALUES_INC) {
        initTemperature = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_INIT_TEMPERATURE));
        minTemperature = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_MIN_TEMPERATURE));
        maxTemperature = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_MAX_TEMPERATURE));
        prefEdgeLength = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_PREF_EDGE_LENGTH));
        gravitation = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_GRAVITATION));
        randomImpulseRange = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_RANDOM_IMPULSE_RANGE));
        overlapDetectWidth = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_OVERLAPPING_DETECTION_WIDTH));
        overlapPrefDistance = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_OVERLAPPING_PREF_DISTANCE));
        shouldEndPerAverage = isTrue((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_COMPUTE_PERMUTATION));
        shouldComputePermutation = isTrue((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_END_CONDITION_AVERAGE));
        avoidOverlapping = isTrue((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_AVOID_OVERLAPPING));
        alphaOsc = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_ALPHA_OSC));
        alphaRot = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_ALPHA_ROT));
        sigmaOsc = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_SIGMA_OSC));
        sigmaRot = Double.parseDouble((String) config.get(//gets 1/x 
        GEMLayoutSettings.KEY_LAYOUT_UPDATE_SIGMA_ROT));
        useOptimizeAlgorithm = isTrue((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_OPTIMIZE_ALGORITHM_ENABLED));
        optimizationAlgorithmConfig = (Properties) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_OPTIMIZE_ALGORITHM_CONFIG);
        perimeterInitSize = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_METHOD_PERIMETER_INIT_SIZE));
        perimeterSizeInc = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_METHOD_PERIMETER_SIZE_INC));
        isClusteringEnabled = isTrue((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_CLUSTERING_ENABLED));
        clusterInitTemperature = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_CLUSTERING_INIT_TEMPERATURE));
        clusterForceScalingFactor = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_CLUSTERING_FORCE_SCALING_FACTOR));
        clusteringFactor = Double.parseDouble((String) config.get(GEMLayoutSettings.KEY_LAYOUT_UPDATE_CLUSTERING_FACTOR));
    }
    //with that line sigmaRot will be 1/(x*cellCount) with x is configurable 
    sigmaRot *= 1.0 / (applyCellList.size() == 0 ? 1 : applyCellList.size());
}
