/******************************************************************************/
public int getLayoutUpdateCostFunctionConfiguration() {
    int config = 0;
    if (cb_lu_useNodeDistance.isSelected()) {
        config |= 32;
    }
    if (cb_lu_useNodeDistribution.isSelected()) {
        config |= 16;
    }
    if (cb_lu_useBorderline.isSelected()) {
        config |= 8;
    }
    if (cb_lu_useEdgeLength.isSelected()) {
        config |= 4;
    }
    if (cb_lu_useEdgeCrossing.isSelected()) {
        config |= 2;
    }
    if (cb_lu_useEdgeDistribution.isSelected()) {
        config |= 1;
    }
    config |= getLayoutUpdateAdditionalCostFunctionConfiguration();
    return config;
}
