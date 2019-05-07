/******************************************************************************/
/**
 * Calculates the current impulse for the given cell.
 * 
 * @param view Cell, the current impulse should be calculated
 * @see #computeImpulse(CellView)
 */
private void computeCurrentImpulse(CellView view) {
    //gets the impulse for view 
    Point2D.Double impulse = computeImpulse(view);
    //set result into node 
    view.getAttributes().put(KEY_CURRENT_IMPULSE, impulse);
}
