/**
     * Draws the label for one axis.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area
     * @param value  the value of the label (ignored).
     * @param cat  the category (zero-based index).
     * @param startAngle  the starting angle.
     * @param extent  the extent of the arc.
     */
protected void drawLabel(Graphics2D g2, Rectangle2D plotArea, double value, int cat, double startAngle, double extent) {
    FontRenderContext frc = g2.getFontRenderContext();
    String label = null;
    if (this.dataExtractOrder == TableOrder.BY_ROW) {
        // if series are in rows, then the categories are the column keys  
        label = this.labelGenerator.generateColumnLabel(this.dataset, cat);
    } else {
        // if series are in columns, then the categories are the row keys  
        label = this.labelGenerator.generateRowLabel(this.dataset, cat);
    }
    Rectangle2D labelBounds = getLabelFont().getStringBounds(label, frc);
    LineMetrics lm = getLabelFont().getLineMetrics(label, frc);
    double ascent = lm.getAscent();
    Point2D labelLocation = calculateLabelLocation(labelBounds, ascent, plotArea, startAngle);
    Composite saveComposite = g2.getComposite();
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    g2.setPaint(getLabelPaint());
    g2.setFont(getLabelFont());
    g2.drawString(label, (float) labelLocation.getX(), (float) labelLocation.getY());
    g2.setComposite(saveComposite);
}
