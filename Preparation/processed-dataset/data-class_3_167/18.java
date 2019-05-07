/**
	 * Return all root cells that intersect the given rectangle.
	 */
public CellView[] getRoots(Rectangle2D clip) {
    java.util.List result = new ArrayList();
    CellView[] views = getRoots();
    for (int i = 0; i < views.length; i++) if (views[i].getBounds().intersects(clip))
        result.add(views[i]);
    views = new CellView[result.size()];
    result.toArray(views);
    return views;
}
