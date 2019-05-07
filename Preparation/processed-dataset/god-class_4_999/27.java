/**
	 * Paint the specified label for the current edgeview.
	 */
protected void paintLabel(Graphics g, String label, Point2D p, boolean mainLabel) {
    if (labelsEnabled && p != null && label != null && label.length() > 0 && metrics != null) {
        int sw = metrics.stringWidth(label);
        int sh = metrics.getHeight();
        Graphics2D g2 = (Graphics2D) g;
        boolean applyTransform = isLabelTransform(label);
        double angle = 0;
        int dx = -sw / 2;
        int offset = isMoveBelowZero || applyTransform ? 0 : Math.min(0, (int) (dx + p.getX()));
        g2.translate(p.getX() - offset, p.getY());
        if (applyTransform) {
            angle = getLabelAngle(label);
            g2.rotate(angle);
        }
        if (isOpaque() && mainLabel) {
            g.setColor(getBackground());
            g.fillRect(-sw / 2 - 1, -sh / 2 - 1, sw + 2, sh + 2);
        }
        if (borderColor != null && mainLabel) {
            g.setColor(borderColor);
            g.drawRect(-sw / 2 - 1, -sh / 2 - 1, sw + 2, sh + 2);
        }
        int dy = +sh / 4;
        g.setColor(fontColor);
        if (applyTransform && borderColor == null && !isOpaque()) {
            // Shift label perpendicularly by the descent so it 
            // doesn't cross the line. 
            dy = -metrics.getDescent();
        }
        g.drawString(label, dx, dy);
        if (applyTransform) {
            // Undo the transform 
            g2.rotate(-angle);
        }
        g2.translate(-p.getX() + offset, -p.getY());
    }
}
