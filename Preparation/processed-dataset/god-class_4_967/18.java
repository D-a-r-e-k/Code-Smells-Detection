/******************************************************************************/
protected void action_CheckBoxSwitch() {
    tf_lambdaNodeDistribution.setEnabled(cb_useNodeDistribution.isSelected());
    tf_lambdaBorderline.setEnabled(cb_useBorderline.isSelected());
    button_takeViewportSize.setEnabled(cb_useBorderline.isSelected());
    tf_boundsWidth.setEnabled(cb_useBorderline.isSelected());
    tf_boundsHeight.setEnabled(cb_useBorderline.isSelected());
    tf_lambdaEdgeLength.setEnabled(cb_useEdgeLength.isSelected());
    tf_lambdaEdgeCrossing.setEnabled(cb_useEdgeCrossing.isSelected());
    tf_lambdaEdgeDistribution.setEnabled(cb_useEdgeDistribution.isSelected());
    tf_lambdaNodeDistance.setEnabled(cb_useNodeDistance.isSelected());
    tf_lu_lambdaNodeDistribution.setEnabled(cb_lu_useNodeDistribution.isSelected());
    tf_lu_lambdaBorderline.setEnabled(cb_lu_useBorderline.isSelected());
    button_lu_takeViewportSize.setEnabled(cb_lu_useBorderline.isSelected());
    tf_lu_boundsWidth.setEnabled(cb_lu_useBorderline.isSelected());
    tf_lu_boundsHeight.setEnabled(cb_lu_useBorderline.isSelected());
    tf_lu_lambdaEdgeLength.setEnabled(cb_lu_useEdgeLength.isSelected());
    tf_lu_lambdaEdgeCrossing.setEnabled(cb_lu_useEdgeCrossing.isSelected());
    tf_lu_lambdaEdgeDistribution.setEnabled(cb_lu_useEdgeDistribution.isSelected());
    tf_lu_lambdaNodeDistance.setEnabled(cb_lu_useNodeDistance.isSelected());
    tf_lu_clustering_factor.setEnabled(cb_lu_clustering_enable.isSelected());
    tf_lu_clustering_moveScale.setEnabled(cb_lu_clustering_enable.isSelected());
    String selectedLUMethod = (String) comb_lu_Method.getSelectedItem();
    if (KEY_LAYOUT_UPDATE_METHOD_NEIGHBORS_ONLY.equals(selectedLUMethod)) {
        tf_lu_method_neighborsDepth.setEnabled(true);
        tf_lu_method_perimeterRadius.setEnabled(false);
        tf_lu_method_perimeterRadiusInc.setEnabled(false);
    } else if (KEY_LAYOUT_UPDATE_METHOD_PERIMETER.equals(selectedLUMethod)) {
        tf_lu_method_neighborsDepth.setEnabled(true);
        tf_lu_method_perimeterRadius.setEnabled(true);
        tf_lu_method_perimeterRadiusInc.setEnabled(true);
    }
}
