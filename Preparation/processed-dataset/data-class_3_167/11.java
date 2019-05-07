/**
	 * Returns the bounding box for the specified cell views.
	 */
public static Rectangle2D getBounds(CellView[] views) {
    if (views != null && views.length > 0) {
        Rectangle2D r = (views[0] != null) ? views[0].getBounds() : null;
        Rectangle2D ret = (r != null) ? (Rectangle2D) r.clone() : null;
        for (int i = 1; i < views.length; i++) {
            r = (views[i] != null) ? views[i].getBounds() : null;
            if (r != null) {
                if (ret == null)
                    ret = (r != null) ? (Rectangle2D) r.clone() : null;
                else
                    Rectangle2D.union(ret, r, ret);
            }
        }
        return ret;
    }
    return null;
}
