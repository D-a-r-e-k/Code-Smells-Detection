/**
	 * Returns the label bounds of the specified view in the given graph.
	 */
public Rectangle2D getLabelBounds(Point2D p, Dimension d, String label) {
    if (label != null && isLabelTransform(label)) {
        // With transform label is rotated, so we should 
        // rotate the rectangle (sw, sh) and return the 
        // bounding rectangle 
        double angle = getLabelAngle(label);
        if (angle < 0)
            angle = -angle;
        if (angle > Math.PI / 2)
            angle %= Math.PI / 2;
        double yside = Math.abs(Math.cos(angle) * d.height + Math.sin(angle) * d.width);
        double xside = Math.abs(d.width * Math.cos(angle) + d.height * Math.sin(angle));
        // Getting maximum is not good, but I don't want to be 
        // drown in calculations 
        if (xside > yside)
            yside = xside;
        if (yside > xside)
            xside = yside;
        angle = getLabelAngle(label);
        // Increasing by height is safe, but I think the precise 
        // value is font.descent layed on edge vector and 
        // projected on each axis 
        d.width = (int) xside + d.height;
        d.height = (int) yside + d.height;
    }
    if (p != null && d != null) {
        double x = Math.max(0, p.getX() - (d.width / 2));
        double y = Math.max(0, p.getY() - (d.height / 2));
        return new Rectangle2D.Double(x, y, d.width + 1, d.height + 1);
    }
    return null;
}
