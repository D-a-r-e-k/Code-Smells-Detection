/******************************************************************************/
/**
     * Loads the initial Values from the gpConfiguration.
     * 
     * @param configSwitch Determines which configurationvalues have to be loaded
     * Possible values are {@link #CONFIG_KEY_RUN} and 
     * {@link #CONFIG_KEY_LAYOUT_UPDATE}
     */
private void loadConfiguration(int configSwitch) {
    //load config for normal runs 
    if (configSwitch == CONFIG_KEY_RUN) {
        initTemperature = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_INIT_TEMPERATURE));
        minTemperature = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_MIN_TEMPERATURE));
        minDistance = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_MIN_DISTANCE));
        tempScaleFactor = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_TEMP_SCALE_FACTOR));
        maxRounds = Integer.parseInt((String) presetConfig.get(AnnealingLayoutSettings.KEY_MAX_ROUNDS));
        triesPerCell = Integer.parseInt((String) presetConfig.get(AnnealingLayoutSettings.KEY_TRIES_PER_CELL));
        ArrayList lambda = (ArrayList) presetConfig.get(AnnealingLayoutSettings.KEY_LAMBDA);
        lambdaList = new double[COUT_COSTFUNCTION];
        for (int i = 0; i < lambdaList.length; i++) lambdaList[i] = ((Double) lambda.get(i)).doubleValue();
        bounds = (Rectangle) presetConfig.get(AnnealingLayoutSettings.KEY_BOUNDS);
        costFunctionConfig = Integer.parseInt((String) presetConfig.get(AnnealingLayoutSettings.KEY_COST_FUNCTION_CONFIG), 2);
        computePermutation = isTrue((String) presetConfig.get(AnnealingLayoutSettings.KEY_COMPUTE_PERMUTATION));
        uphillMovesAllowed = isTrue((String) presetConfig.get(AnnealingLayoutSettings.KEY_IS_UPHILL_MOVE_ALLOWED));
    } else if (configSwitch == CONFIG_KEY_LAYOUT_UPDATE) {
        initTemperature = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_INIT_TEMPERATURE));
        minTemperature = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_MIN_TEMPERATURE));
        minDistance = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_MIN_DISTANCE));
        tempScaleFactor = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_TEMP_SCALE_FACTOR));
        maxRounds = Integer.parseInt((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_MAX_ROUNDS));
        triesPerCell = Integer.parseInt((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_TRIES_PER_CELL));
        ArrayList lambda = (ArrayList) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_LAMBDA);
        lambdaList = new double[COUT_COSTFUNCTION];
        for (int i = 0; i < lambdaList.length; i++) lambdaList[i] = ((Double) lambda.get(i)).doubleValue();
        bounds = (Rectangle) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_BOUNDS);
        costFunctionConfig = Integer.parseInt((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_COST_FUNCTION_CONFIG), 2);
        computePermutation = isTrue((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_COMPUTE_PERMUTATION));
        uphillMovesAllowed = isTrue((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_IS_UPHILL_MOVE_ALLOWED));
        luRecursionDepth = Integer.parseInt((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_METHOD_NEIGHBORS_DEPTH));
        luPerimeterRadius = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_METHOD_PERIMETER_RADIUS));
        luPerimeterRadiusInc = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_METHOD_PERIMETER_RADIUS_INCREASE));
        luMethod = (String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_METHOD);
        isClusteringEnabled = isTrue((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_CLUSTERING_ENABLED));
        clusteringFactor = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_CLUSTERING_FACTOR));
        clusterMoveScaleFactor = Double.parseDouble((String) presetConfig.get(AnnealingLayoutSettings.KEY_LAYOUT_UPDATE_CLUSTERING_MOVE_SCALE));
    }
}
