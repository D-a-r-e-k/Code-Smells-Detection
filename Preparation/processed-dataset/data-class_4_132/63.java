/******************************************************************************/
public void setLayoutUpdateCostFunctionConfiguration(int config) {
    cb_lu_useNodeDistance.setSelected((config & 32) != 0);
    cb_lu_useNodeDistribution.setSelected((config & 16) != 0);
    cb_lu_useBorderline.setSelected((config & 8) != 0);
    cb_lu_useEdgeLength.setSelected((config & 4) != 0);
    cb_lu_useEdgeCrossing.setSelected((config & 2) != 0);
    cb_lu_useEdgeDistribution.setSelected((config & 1) != 0);
}
