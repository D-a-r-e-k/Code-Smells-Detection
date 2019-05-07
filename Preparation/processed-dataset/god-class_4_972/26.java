/******************************************************************************/
/**
 * Initialilzes the position of a CellView to the center point of the bounds
 * of the cell. This initialization is only be done, when the cell isn't 
 * initialised before.
 * 
 * @param view Cell, the position should be initialized.
 */
private void initPosition(CellView view) {
    if (!view.getAttributes().containsKey(KEY_POSITION))
        view.getAttributes().put(KEY_POSITION, new Point2D.Double(view.getBounds().getCenterX(), view.getBounds().getCenterY()));
}
