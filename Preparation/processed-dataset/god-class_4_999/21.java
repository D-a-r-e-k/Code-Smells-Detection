/**
	 * Paints the edge itself
	 * @param g the graphics object being painted to
	 */
protected void paintEdge(Graphics g) {
    g.setColor(getForeground());
    if (lineWidth > 0) {
        Graphics2D g2 = (Graphics2D) g;
        int c = BasicStroke.CAP_BUTT;
        int j = BasicStroke.JOIN_MITER;
        g2.setStroke(new BasicStroke(lineWidth, c, j));
        if (gradientColor != null && !preview) {
            g2.setPaint(new GradientPaint(0, 0, getBackground(), getWidth(), getHeight(), gradientColor, true));
        }
        if (view.beginShape != null) {
            if (beginFill)
                g2.fill(view.beginShape);
            g2.draw(view.beginShape);
        }
        if (view.endShape != null) {
            if (endFill)
                g2.fill(view.endShape);
            g2.draw(view.endShape);
        }
        if (lineDash != null)
            // Dash For Line Only 
            g2.setStroke(new BasicStroke(lineWidth, c, j, 10.0f, lineDash, dashOffset));
        if (view.lineShape != null)
            g2.draw(view.lineShape);
    }
}
