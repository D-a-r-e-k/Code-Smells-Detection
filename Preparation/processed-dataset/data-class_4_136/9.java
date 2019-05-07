/******************************************************************************/
/**
 * Sets the initial values for one Cell.
 * 
 * @param view Cell, the initial values should be set for.
 */
private void initializeVertice(CellView view) {
    Map attributes = view.getAttributes();
    if (attributes == null)
        attributes = new Hashtable();
    attributes.put(KEY_CAPTION, KEY_CAPTION);
    initPosition(view);
    if (isCluster(view)) {
        attributes.put(KEY_TEMPERATURE, new Double(clusterInitTemperature));
    } else
        attributes.put(KEY_TEMPERATURE, new Double(initTemperature));
    attributes.put(KEY_SKEWGAUGE, new Double(0.0));
    attributes.put(KEY_CURRENT_IMPULSE, new Point2D.Double());
    attributes.put(KEY_LAST_IMPULSE, new Point2D.Double());
}
