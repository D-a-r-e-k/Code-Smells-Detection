/******************************************************************************/
public int getCostFunctionConfiguration() {
    int config = 0;
    if (cb_useNodeDistance.isSelected()) {
        config |= 32;
    }
    if (cb_useNodeDistribution.isSelected()) {
        config |= 16;
    }
    if (cb_useBorderline.isSelected()) {
        config |= 8;
    }
    if (cb_useEdgeLength.isSelected()) {
        config |= 4;
    }
    if (cb_useEdgeCrossing.isSelected()) {
        config |= 2;
    }
    if (cb_useEdgeDistribution.isSelected()) {
        config |= 1;
    }
    config |= getAdditionalCostFunctionConfiguration();
    return config;
}
