/**
     * Tests this plot for equality with an arbitrary object.
     *
     * @param obj  the object (<code>null</code> permitted).
     *
     * @return A boolean.
     */
public boolean equals(Object obj) {
    if (obj == this) {
        return true;
    }
    if (!(obj instanceof SpiderWebPlot)) {
        return false;
    }
    if (!super.equals(obj)) {
        return false;
    }
    SpiderWebPlot that = (SpiderWebPlot) obj;
    if (!this.dataExtractOrder.equals(that.dataExtractOrder)) {
        return false;
    }
    if (this.headPercent != that.headPercent) {
        return false;
    }
    if (this.interiorGap != that.interiorGap) {
        return false;
    }
    if (this.startAngle != that.startAngle) {
        return false;
    }
    if (!this.direction.equals(that.direction)) {
        return false;
    }
    if (this.maxValue != that.maxValue) {
        return false;
    }
    if (this.webFilled != that.webFilled) {
        return false;
    }
    if (this.axisLabelGap != that.axisLabelGap) {
        return false;
    }
    if (!PaintUtilities.equal(this.axisLinePaint, that.axisLinePaint)) {
        return false;
    }
    if (!this.axisLineStroke.equals(that.axisLineStroke)) {
        return false;
    }
    if (!ShapeUtilities.equal(this.legendItemShape, that.legendItemShape)) {
        return false;
    }
    if (!PaintUtilities.equal(this.seriesPaint, that.seriesPaint)) {
        return false;
    }
    if (!this.seriesPaintList.equals(that.seriesPaintList)) {
        return false;
    }
    if (!PaintUtilities.equal(this.baseSeriesPaint, that.baseSeriesPaint)) {
        return false;
    }
    if (!PaintUtilities.equal(this.seriesOutlinePaint, that.seriesOutlinePaint)) {
        return false;
    }
    if (!this.seriesOutlinePaintList.equals(that.seriesOutlinePaintList)) {
        return false;
    }
    if (!PaintUtilities.equal(this.baseSeriesOutlinePaint, that.baseSeriesOutlinePaint)) {
        return false;
    }
    if (!ObjectUtilities.equal(this.seriesOutlineStroke, that.seriesOutlineStroke)) {
        return false;
    }
    if (!this.seriesOutlineStrokeList.equals(that.seriesOutlineStrokeList)) {
        return false;
    }
    if (!this.baseSeriesOutlineStroke.equals(that.baseSeriesOutlineStroke)) {
        return false;
    }
    if (!this.labelFont.equals(that.labelFont)) {
        return false;
    }
    if (!PaintUtilities.equal(this.labelPaint, that.labelPaint)) {
        return false;
    }
    if (!this.labelGenerator.equals(that.labelGenerator)) {
        return false;
    }
    if (!ObjectUtilities.equal(this.toolTipGenerator, that.toolTipGenerator)) {
        return false;
    }
    if (!ObjectUtilities.equal(this.urlGenerator, that.urlGenerator)) {
        return false;
    }
    return true;
}
