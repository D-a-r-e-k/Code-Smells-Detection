/******************************************************************************/
public void setCostFunctionConfiguration(int config) {
    cb_useNodeDistance.setSelected((config & 32) != 0);
    cb_useNodeDistribution.setSelected((config & 16) != 0);
    cb_useBorderline.setSelected((config & 8) != 0);
    cb_useEdgeLength.setSelected((config & 4) != 0);
    cb_useEdgeCrossing.setSelected((config & 2) != 0);
    cb_useEdgeDistribution.setSelected((config & 1) != 0);
}
