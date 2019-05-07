/**
	 * Paints any selection effect applied to the edge
	 * @param g the graphics object being painted to
	 */
protected void paintSelection(Graphics g) {
    if (selected) {
        // Paint Selected 
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(GraphConstants.SELECTION_STROKE);
        g2.setColor(highlightColor);
        if (view.beginShape != null)
            g2.draw(view.beginShape);
        if (view.lineShape != null)
            g2.draw(view.lineShape);
        if (view.endShape != null)
            g2.draw(view.endShape);
    }
}
