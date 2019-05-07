/******************************************************************************/
private void switchLayoutUpdatePanel() {
    if (cb_enableLayoutUpdate.isSelected() && !isOptimizer) {
        tp_main.addTab("Values for Layout updates", panelLUSurfaceWrapper);
    } else {
        tp_main.remove(panelLUSurfaceWrapper);
    }
}
