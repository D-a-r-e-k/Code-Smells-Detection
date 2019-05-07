/**
     * Returns a clone of this plot.
     *
     * @return A clone of this plot.
     *
     * @throws CloneNotSupportedException if the plot cannot be cloned for
     *         any reason.
     */
public Object clone() throws CloneNotSupportedException {
    SpiderWebPlot clone = (SpiderWebPlot) super.clone();
    clone.legendItemShape = ShapeUtilities.clone(this.legendItemShape);
    clone.seriesPaintList = (PaintList) this.seriesPaintList.clone();
    clone.seriesOutlinePaintList = (PaintList) this.seriesOutlinePaintList.clone();
    clone.seriesOutlineStrokeList = (StrokeList) this.seriesOutlineStrokeList.clone();
    return clone;
}
