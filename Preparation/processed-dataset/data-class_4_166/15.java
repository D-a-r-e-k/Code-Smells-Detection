/**
	 * Returns the label size of the specified view in the given graph.
	 */
public Dimension getLabelSize(EdgeView view, String label) {
    if (label != null && fontGraphics != null) {
        fontGraphics.setFont(GraphConstants.getFont(view.getAllAttributes()));
        metrics = fontGraphics.getFontMetrics();
        int sw = (int) (metrics.stringWidth(label) * LABELWIDTHBUFFER);
        int sh = metrics.getHeight();
        return new Dimension(sw, sh);
    }
    return null;
}
