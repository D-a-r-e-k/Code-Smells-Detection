public void setConfiguration(Properties config) {
    // Read config 
    setInitTemperature(Double.parseDouble((String) config.get(KEY_INIT_TEMPERATURE)));
    setMinTemperature(Double.parseDouble((String) config.get(KEY_MIN_TEMPERATURE)));
    setMinDistance(Double.parseDouble((String) config.get(KEY_MIN_DISTANCE)));
    setTemperatureScaleFactor(Double.parseDouble((String) config.get(KEY_TEMP_SCALE_FACTOR)));
    setComputePermutation(isTrue((String) config.get(KEY_COMPUTE_PERMUTATION)));
    setUphillMovesAllowed(isTrue((String) config.get(KEY_IS_UPHILL_MOVE_ALLOWED)));
    setMaxRounds(Integer.parseInt((String) config.get(KEY_MAX_ROUNDS)));
    setTriesPerCell(Integer.parseInt((String) config.get(KEY_TRIES_PER_CELL)));
    setCostFunctionConfiguration(Integer.parseInt((String) config.get(KEY_COST_FUNCTION_CONFIG), 2));
    setLambda((ArrayList) config.get(KEY_LAMBDA));
    setResultBounds((Rectangle) config.get(KEY_BOUNDS));
    setLayoutUpdateEnabled(isTrue((String) config.get(KEY_LAYOUT_UPDATE_ENABLED)));
    setLayoutUpdateInitTemperature(Double.parseDouble((String) config.get(KEY_LAYOUT_UPDATE_INIT_TEMPERATURE)));
    setLayoutUpdateMinTemperature(Double.parseDouble((String) config.get(KEY_LAYOUT_UPDATE_MIN_TEMPERATURE)));
    setLayoutUpdateMinDistance(Double.parseDouble((String) config.get(KEY_LAYOUT_UPDATE_MIN_DISTANCE)));
    setLayoutUpdateTemperatureScaleFactor(Double.parseDouble((String) config.get(KEY_LAYOUT_UPDATE_TEMP_SCALE_FACTOR)));
    setLayoutUpdateComputePermutation(isTrue((String) config.get(KEY_LAYOUT_UPDATE_COMPUTE_PERMUTATION)));
    setLayoutUpdateUphillMovesAllowed(isTrue((String) config.get(KEY_LAYOUT_UPDATE_IS_UPHILL_MOVE_ALLOWED)));
    setLayoutUpdateMaxRounds(Integer.parseInt((String) config.get(KEY_LAYOUT_UPDATE_MAX_ROUNDS)));
    setLayoutUpdateTriesPerCell(Integer.parseInt((String) config.get(KEY_LAYOUT_UPDATE_TRIES_PER_CELL)));
    setLayoutUpdateCostFunctionConfiguration(Integer.parseInt((String) config.get(KEY_LAYOUT_UPDATE_COST_FUNCTION_CONFIG), 2));
    setLayoutUpdateLambda((ArrayList) config.get(KEY_LAYOUT_UPDATE_LAMBDA));
    setLayoutUpdateResultBounds((Rectangle) config.get(KEY_LAYOUT_UPDATE_BOUNDS));
    setLayoutUpdateMethod((String) config.get(KEY_LAYOUT_UPDATE_METHOD));
    setLayoutUpdateMethodNeighborsDepth(Integer.parseInt((String) config.get(KEY_LAYOUT_UPDATE_METHOD_NEIGHBORS_DEPTH)));
    setLayoutUpdateMethodPerimeterRadius(Double.parseDouble((String) config.get(KEY_LAYOUT_UPDATE_METHOD_PERIMETER_RADIUS)));
    setLayoutUpdateMethodPerimeterRadiusIncrease(Double.parseDouble((String) config.get(KEY_LAYOUT_UPDATE_METHOD_PERIMETER_RADIUS_INCREASE)));
    setLayoutUpdateClusteringEnabled(isTrue((String) config.get(KEY_LAYOUT_UPDATE_CLUSTERING_ENABLED)));
    setLayoutUpdateClusteringFactor(Double.parseDouble((String) config.get(KEY_LAYOUT_UPDATE_CLUSTERING_FACTOR)));
    setLayoutUpdateClusteringMoveScaleFactor(Double.parseDouble((String) config.get(KEY_LAYOUT_UPDATE_CLUSTERING_MOVE_SCALE)));
}
