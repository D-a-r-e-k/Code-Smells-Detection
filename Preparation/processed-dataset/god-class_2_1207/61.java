/**
     * Returns the location for a label
     *
     * @param labelBounds the label bounds.
     * @param ascent the ascent (height of font).
     * @param plotArea the plot area
     * @param startAngle the start angle for the pie series.
     *
     * @return The location for a label.
     */
protected Point2D calculateLabelLocation(Rectangle2D labelBounds, double ascent, Rectangle2D plotArea, double startAngle) {
    Arc2D arc1 = new Arc2D.Double(plotArea, startAngle, 0, Arc2D.OPEN);
    Point2D point1 = arc1.getEndPoint();
    double deltaX = -(point1.getX() - plotArea.getCenterX()) * this.axisLabelGap;
    double deltaY = -(point1.getY() - plotArea.getCenterY()) * this.axisLabelGap;
    double labelX = point1.getX() - deltaX;
    double labelY = point1.getY() - deltaY;
    if (labelX < plotArea.getCenterX()) {
        labelX -= labelBounds.getWidth();
    }
    if (labelX == plotArea.getCenterX()) {
        labelX -= labelBounds.getWidth() / 2;
    }
    if (labelY > plotArea.getCenterY()) {
        labelY += ascent;
    }
    return new Point2D.Double(labelX, labelY);
}
