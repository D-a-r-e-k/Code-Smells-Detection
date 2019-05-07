/**
     * Sets the outline paint for ALL series in the plot. If this is set to
     * </code> null</code>, then a list of paints is used instead (to allow
     * different colors to be used for each series).
     *
     * @param paint  the paint (<code>null</code> permitted).
     */
public void setSeriesOutlinePaint(Paint paint) {
    this.seriesOutlinePaint = paint;
    fireChangeEvent();
}
