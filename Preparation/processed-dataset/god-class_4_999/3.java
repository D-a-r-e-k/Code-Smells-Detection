/**
	 * Returns true if the edge shape intersects the given rectangle.
	 */
public boolean intersects(JGraph graph, CellView value, Rectangle rect) {
    if (value instanceof EdgeView && graph != null && value != null) {
        setView(value);
        // If we have two control points, we can get rid of hit 
        // detection on do an intersection test on the two diagonals 
        // of rect and the line between the two points 
        Graphics2D g2 = (Graphics2D) graph.getGraphics();
        EdgeView edgeView = (EdgeView) value;
        if (g2 == null || edgeView.getPointCount() == 2) {
            Point2D p0 = edgeView.getPoint(0);
            Point2D p1 = edgeView.getPoint(1);
            if (rect.intersectsLine(p0.getX(), p0.getY(), p1.getX(), p1.getY()))
                return true;
        } else if (g2 != null) {
            if (g2.hit(rect, view.getShape(), true))
                return true;
        }
        Rectangle2D r = getLabelBounds(graph, view);
        if (r != null && r.intersects(rect)) {
            boolean hits = true;
            // Performs exact hit detection on rotated labels 
            if (HIT_LABEL_EXACT) {
                AffineTransform tx = g2.getTransform();
                try {
                    String lab = graph.convertValueToString(view);
                    Point2D tmpPt = getLabelPosition(view);
                    Dimension size = getLabelSize(view, lab);
                    Rectangle2D tmp = new Rectangle((int) tmpPt.getX(), (int) tmpPt.getY(), size.width, size.height);
                    double cx = tmp.getCenterX();
                    double cy = tmp.getCenterY();
                    g2.translate(-size.width / 2, -size.height * 0.75 - metrics.getDescent());
                    boolean applyTransform = isLabelTransform(lab);
                    double angle = 0;
                    if (applyTransform) {
                        angle = getLabelAngle(lab);
                        g2.rotate(angle, cx, cy);
                    }
                    hits = g2.hit(rect, tmp, false);
                } finally {
                    g2.setTransform(tx);
                }
            }
            if (hits) {
                return true;
            }
        }
        Object[] labels = GraphConstants.getExtraLabels(view.getAllAttributes());
        if (labels != null) {
            for (int i = 0; i < labels.length; i++) {
                r = getExtraLabelBounds(graph, view, i);
                if (r != null && r.intersects(rect))
                    return true;
            }
        }
    }
    return false;
}
