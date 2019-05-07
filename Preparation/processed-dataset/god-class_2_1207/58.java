/**
     * Draws a radar plot polygon.
     *
     * @param g2 the graphics device.
     * @param plotArea the area we are plotting in (already adjusted).
     * @param centre the centre point of the radar axes
     * @param info chart rendering info.
     * @param series the series within the dataset we are plotting
     * @param catCount the number of categories per radar plot
     * @param headH the data point height
     * @param headW the data point width
     */
protected void drawRadarPoly(Graphics2D g2, Rectangle2D plotArea, Point2D centre, PlotRenderingInfo info, int series, int catCount, double headH, double headW) {
    Polygon polygon = new Polygon();
    EntityCollection entities = null;
    if (info != null) {
        entities = info.getOwner().getEntityCollection();
    }
    // plot the data...  
    for (int cat = 0; cat < catCount; cat++) {
        Number dataValue = getPlotValue(series, cat);
        if (dataValue != null) {
            double value = dataValue.doubleValue();
            if (value >= 0) {
                // draw the polygon series...  
                // Finds our starting angle from the centre for this axis  
                double angle = getStartAngle() + (getDirection().getFactor() * cat * 360 / catCount);
                // The following angle calc will ensure there isn't a top  
                // vertical axis - this may be useful if you don't want any  
                // given criteria to 'appear' move important than the  
                // others..  
                //  + (getDirection().getFactor()  
                //        * (cat + 0.5) * 360 / catCount);  
                // find the point at the appropriate distance end point  
                // along the axis/angle identified above and add it to the  
                // polygon  
                Point2D point = getWebPoint(plotArea, angle, value / this.maxValue);
                polygon.addPoint((int) point.getX(), (int) point.getY());
                // put an elipse at the point being plotted..  
                Paint paint = getSeriesPaint(series);
                Paint outlinePaint = getSeriesOutlinePaint(series);
                Stroke outlineStroke = getSeriesOutlineStroke(series);
                Ellipse2D head = new Ellipse2D.Double(point.getX() - headW / 2, point.getY() - headH / 2, headW, headH);
                g2.setPaint(paint);
                g2.fill(head);
                g2.setStroke(outlineStroke);
                g2.setPaint(outlinePaint);
                g2.draw(head);
                if (entities != null) {
                    int row = 0;
                    int col = 0;
                    if (this.dataExtractOrder == TableOrder.BY_ROW) {
                        row = series;
                        col = cat;
                    } else {
                        row = cat;
                        col = series;
                    }
                    String tip = null;
                    if (this.toolTipGenerator != null) {
                        tip = this.toolTipGenerator.generateToolTip(this.dataset, row, col);
                    }
                    String url = null;
                    if (this.urlGenerator != null) {
                        url = this.urlGenerator.generateURL(this.dataset, row, col);
                    }
                    Shape area = new Rectangle((int) (point.getX() - headW), (int) (point.getY() - headH), (int) (headW * 2), (int) (headH * 2));
                    CategoryItemEntity entity = new CategoryItemEntity(area, tip, url, this.dataset, this.dataset.getRowKey(row), this.dataset.getColumnKey(col));
                    entities.add(entity);
                }
            }
        }
    }
    // Plot the polygon  
    Paint paint = getSeriesPaint(series);
    g2.setPaint(paint);
    g2.setStroke(getSeriesOutlineStroke(series));
    g2.draw(polygon);
    // Lastly, fill the web polygon if this is required  
    if (this.webFilled) {
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        g2.fill(polygon);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getForegroundAlpha()));
    }
}
