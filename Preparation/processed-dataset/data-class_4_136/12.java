/******************************************************************************/
/**
 * Calculates the last impulse for the given cell. This is only nesessary while
 * initializing the cells.
 * 
 * @param view Cell, the last impulse should be calculated
 * @see #computeImpulse(CellView)
 */
private void computeLastImpulse(CellView view) {
    //gets the impulse for view 
    Point2D.Double impulse = computeImpulse(view);
    //set result into node 
    view.getAttributes().put(KEY_LAST_IMPULSE, impulse);
}
