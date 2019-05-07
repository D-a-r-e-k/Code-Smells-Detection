/******************************************************************************/
/** Called by pressing the ok button
     *
     */
protected void check() {
    boolean isOK = true;
    ArrayList errList = new ArrayList();
    isOK &= assertDouble(tf_initTemperature.getText(), KEY_INIT_TEMPERATURE, errList);
    isOK &= assertDouble(tf_minTemperature.getText(), KEY_MIN_TEMPERATURE, errList);
    isOK &= assertDouble(tf_minDistance.getText(), KEY_MIN_DISTANCE, errList);
    isOK &= assertDouble(tf_tempScaleFactor.getText(), KEY_TEMP_SCALE_FACTOR, errList);
    isOK &= assertInteger(tf_maxRounds.getText(), KEY_MAX_ROUNDS, errList);
    isOK &= assertInteger(tf_triesPerCell.getText(), KEY_TRIES_PER_CELL, errList);
    isOK &= assertDouble(tf_lambdaNodeDistribution.getText(), "Node Distribution", errList);
    isOK &= assertDouble(tf_lambdaBorderline.getText(), "Borderline", errList);
    isOK &= assertDouble(tf_lambdaEdgeLength.getText(), "Edgelength", errList);
    isOK &= assertDouble(tf_lambdaEdgeCrossing.getText(), "Edgecrossing", errList);
    isOK &= assertDouble(tf_lambdaEdgeDistribution.getText(), "Node-Edge Distribution", errList);
    isOK &= assertDouble(tf_lambdaNodeDistance.getText(), "Node Overlapping", errList);
    isOK &= assertInteger(tf_boundsWidth.getText(), "max. width", errList);
    isOK &= assertInteger(tf_boundsHeight.getText(), "max. height", errList);
    isOK &= assertDouble(tf_lu_initTemperature.getText(), KEY_LAYOUT_UPDATE_INIT_TEMPERATURE, errList);
    isOK &= assertDouble(tf_lu_minTemperature.getText(), KEY_LAYOUT_UPDATE_MIN_TEMPERATURE, errList);
    isOK &= assertDouble(tf_lu_minDistance.getText(), KEY_LAYOUT_UPDATE_MIN_DISTANCE, errList);
    isOK &= assertDouble(tf_lu_tempScaleFactor.getText(), KEY_LAYOUT_UPDATE_TEMP_SCALE_FACTOR, errList);
    isOK &= assertInteger(tf_lu_maxRounds.getText(), KEY_LAYOUT_UPDATE_MAX_ROUNDS, errList);
    isOK &= assertInteger(tf_lu_triesPerCell.getText(), KEY_LAYOUT_UPDATE_TRIES_PER_CELL, errList);
    isOK &= assertDouble(tf_lu_lambdaNodeDistribution.getText(), "Layout Update Node Distribution", errList);
    isOK &= assertDouble(tf_lu_lambdaBorderline.getText(), "Layout Update Borderline", errList);
    isOK &= assertDouble(tf_lu_lambdaEdgeLength.getText(), "Layout Update Edgelength", errList);
    isOK &= assertDouble(tf_lu_lambdaEdgeCrossing.getText(), "Layout Update Edgecrossing", errList);
    isOK &= assertDouble(tf_lu_lambdaEdgeDistribution.getText(), "Layout Update Node-Edge Distribution", errList);
    isOK &= assertDouble(tf_lu_lambdaNodeDistance.getText(), "Layout Update Node Overlapping", errList);
    isOK &= assertInteger(tf_lu_boundsWidth.getText(), "Layout Update max. width", errList);
    isOK &= assertInteger(tf_lu_boundsHeight.getText(), "Layout Update max. height", errList);
    isOK &= assertDouble(tf_lu_clustering_factor.getText(), KEY_LAYOUT_UPDATE_CLUSTERING_FACTOR, errList);
    isOK &= assertDouble(tf_lu_clustering_moveScale.getText(), KEY_LAYOUT_UPDATE_CLUSTERING_MOVE_SCALE, errList);
    if (isOK) {
        isOK &= assertDoublePositiveSign(tf_initTemperature.getText(), false, KEY_INIT_TEMPERATURE, errList);
        isOK &= assertDoublePositiveSign(tf_initTemperature.getText(), false, KEY_INIT_TEMPERATURE, errList);
        isOK &= assertDoublePositiveSign(tf_minTemperature.getText(), false, KEY_MIN_TEMPERATURE, errList);
        isOK &= assertDoublePositiveSign(tf_minDistance.getText(), false, KEY_MIN_DISTANCE, errList);
        isOK &= assertRange(tf_tempScaleFactor.getText(), 0.0, 1.0, false, false, KEY_TEMP_SCALE_FACTOR, errList);
        isOK &= assertIntegerPositiveSign(tf_maxRounds.getText(), false, KEY_MAX_ROUNDS, errList);
        isOK &= assertRange(tf_triesPerCell.getText(), 8, 99, true, true, KEY_TRIES_PER_CELL, errList);
        isOK &= assertIntegerPositiveSign(tf_boundsWidth.getText(), false, "max. width", errList);
        isOK &= assertIntegerPositiveSign(tf_boundsWidth.getText(), false, "max. height", errList);
        isOK &= assertDoublePositiveSign(tf_lambdaNodeDistribution.getText(), false, "Node Distribution", errList);
        isOK &= assertDoublePositiveSign(tf_lambdaBorderline.getText(), false, "Borderline", errList);
        isOK &= assertDoublePositiveSign(tf_lambdaEdgeLength.getText(), false, "Edgelength", errList);
        isOK &= assertDoublePositiveSign(tf_lambdaEdgeCrossing.getText(), false, "Edgecrossing", errList);
        isOK &= assertDoublePositiveSign(tf_lambdaEdgeDistribution.getText(), false, "Node-Edge Distribution", errList);
        isOK &= assertDoublePositiveSign(tf_lambdaNodeDistance.getText(), false, "Node Overlapping", errList);
        isOK &= assertDoublePositiveSign(tf_lu_initTemperature.getText(), false, KEY_LAYOUT_UPDATE_INIT_TEMPERATURE, errList);
        isOK &= assertDoublePositiveSign(tf_lu_initTemperature.getText(), false, KEY_LAYOUT_UPDATE_INIT_TEMPERATURE, errList);
        isOK &= assertDoublePositiveSign(tf_lu_minTemperature.getText(), false, KEY_LAYOUT_UPDATE_MIN_TEMPERATURE, errList);
        isOK &= assertDoublePositiveSign(tf_lu_minDistance.getText(), false, KEY_LAYOUT_UPDATE_MIN_DISTANCE, errList);
        isOK &= assertRange(tf_lu_tempScaleFactor.getText(), 0.0, 1.0, false, false, KEY_LAYOUT_UPDATE_TEMP_SCALE_FACTOR, errList);
        isOK &= assertIntegerPositiveSign(tf_lu_maxRounds.getText(), false, KEY_LAYOUT_UPDATE_MAX_ROUNDS, errList);
        isOK &= assertRange(tf_lu_triesPerCell.getText(), 8, 99, true, true, KEY_LAYOUT_UPDATE_TRIES_PER_CELL, errList);
        isOK &= assertIntegerPositiveSign(tf_lu_boundsWidth.getText(), false, "Layout Update max. width", errList);
        isOK &= assertIntegerPositiveSign(tf_lu_boundsWidth.getText(), false, "Layout Update max. height", errList);
        isOK &= assertDoublePositiveSign(tf_lu_lambdaNodeDistribution.getText(), false, "Layout Update Node Distribution", errList);
        isOK &= assertDoublePositiveSign(tf_lu_lambdaBorderline.getText(), false, "Layout Update Borderline", errList);
        isOK &= assertDoublePositiveSign(tf_lu_lambdaEdgeLength.getText(), false, "Layout Update Edgelength", errList);
        isOK &= assertDoublePositiveSign(tf_lu_lambdaEdgeCrossing.getText(), false, "Layout Update Edgecrossing", errList);
        isOK &= assertDoublePositiveSign(tf_lu_lambdaEdgeDistribution.getText(), false, "Layout Update Node-Edge Distribution", errList);
        isOK &= assertDoublePositiveSign(tf_lu_lambdaNodeDistance.getText(), false, "Layout Update Node Overlapping", errList);
        isOK &= assertDoublePositiveSign(tf_lu_clustering_factor.getText(), false, KEY_LAYOUT_UPDATE_CLUSTERING_FACTOR, errList);
        isOK &= assertRange(tf_lu_clustering_moveScale.getText(), 0.0, 1.0, false, true, KEY_LAYOUT_UPDATE_CLUSTERING_MOVE_SCALE, errList);
    }
    if (!isOK) {
        String errorMsg = new String();
        for (int i = 0; i < errList.size(); i++) {
            errorMsg += (String) errList.get(i);
            if (i != errList.size() - 1)
                errorMsg += "\n";
        }
        throw new IllegalArgumentException(errorMsg);
    }
}
