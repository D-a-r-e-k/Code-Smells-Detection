// 
// Static Methods 
// 
/**
	 * Translates the specified views by the given amount.
	 * 
	 * @param views
	 *            an array of cell view to each be translated
	 * @param dx
	 *            the amount to translate the views in the x-axis
	 * @param dy
	 *            the amount to translate the views in the x-axis
	 */
public static void translateViews(CellView[] views, double dx, double dy) {
    for (int i = 0; i < views.length; i++) {
        if (views[i] instanceof AbstractCellView) {
            ((AbstractCellView) views[i]).translate(dx, dy);
        }
    }
}
