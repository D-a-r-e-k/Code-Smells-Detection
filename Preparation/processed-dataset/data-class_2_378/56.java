/**
     * Draws the plot on a Java 2D graphics device (such as the screen or a
     * printer).
     *
     * @param g2  the graphics device.
     * @param area  the area within which the plot should be drawn.
     * @param anchor  the anchor point (<code>null</code> permitted).
     * @param parentState  the state from the parent plot, if there is one.
     * @param info  collects info about the drawing.
     */
public void draw(Graphics2D g2, Rectangle2D area, Point2D anchor, PlotState parentState, PlotRenderingInfo info) {
    // adjust for insets...  
    RectangleInsets insets = getInsets();
    insets.trim(area);
    if (info != null) {
        info.setPlotArea(area);
        info.setDataArea(area);
    }
    drawBackground(g2, area);
    drawOutline(g2, area);
    Shape savedClip = g2.getClip();
    g2.clip(area);
    Composite originalComposite = g2.getComposite();
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getForegroundAlpha()));
    if (!DatasetUtilities.isEmptyOrNull(this.dataset)) {
        int seriesCount = 0, catCount = 0;
        if (this.dataExtractOrder == TableOrder.BY_ROW) {
            seriesCount = this.dataset.getRowCount();
            catCount = this.dataset.getColumnCount();
        } else {
            seriesCount = this.dataset.getColumnCount();
            catCount = this.dataset.getRowCount();
        }
        // ensure we have a maximum value to use on the axes  
        if (this.maxValue == DEFAULT_MAX_VALUE)
            calculateMaxValue(seriesCount, catCount);
        // Next, setup the plot area  
        // adjust the plot area by the interior spacing value  
        double gapHorizontal = area.getWidth() * getInteriorGap();
        double gapVertical = area.getHeight() * getInteriorGap();
        double X = area.getX() + gapHorizontal / 2;
        double Y = area.getY() + gapVertical / 2;
        double W = area.getWidth() - gapHorizontal;
        double H = area.getHeight() - gapVertical;
        double headW = area.getWidth() * this.headPercent;
        double headH = area.getHeight() * this.headPercent;
        // make the chart area a square  
        double min = Math.min(W, H) / 2;
        X = (X + X + W) / 2 - min;
        Y = (Y + Y + H) / 2 - min;
        W = 2 * min;
        H = 2 * min;
        Point2D centre = new Point2D.Double(X + W / 2, Y + H / 2);
        Rectangle2D radarArea = new Rectangle2D.Double(X, Y, W, H);
        // draw the axis and category label  
        for (int cat = 0; cat < catCount; cat++) {
            double angle = getStartAngle() + (getDirection().getFactor() * cat * 360 / catCount);
            Point2D endPoint = getWebPoint(radarArea, angle, 1);
            // 1 = end of axis  
            Line2D line = new Line2D.Double(centre, endPoint);
            g2.setPaint(this.axisLinePaint);
            g2.setStroke(this.axisLineStroke);
            g2.draw(line);
            drawLabel(g2, radarArea, 0.0, cat, angle, 360.0 / catCount);
        }
        // Now actually plot each of the series polygons..  
        for (int series = 0; series < seriesCount; series++) {
            drawRadarPoly(g2, radarArea, centre, info, series, catCount, headH, headW);
        }
    } else {
        drawNoDataMessage(g2, area);
    }
    g2.setClip(savedClip);
    g2.setComposite(originalComposite);
    drawOutline(g2, area);
}
