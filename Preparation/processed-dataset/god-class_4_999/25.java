/**
	 * Provided for subclassers to paint a selection border.
	 */
protected void paintSelectionBorder(Graphics g) {
    ((Graphics2D) g).setStroke(GraphConstants.SELECTION_STROKE);
    if (childrenSelected)
        g.setColor(gridColor);
    else if (focus && selected)
        g.setColor(lockedHandleColor);
    else if (selected)
        g.setColor(highlightColor);
    if (childrenSelected || selected) {
        Dimension d = getSize();
        g.drawRect(0, 0, d.width - 1, d.height - 1);
    }
}
