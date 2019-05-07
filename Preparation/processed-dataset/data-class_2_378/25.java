/**
     * Sets the paint for ALL series in the plot. If this is set to</code> null
     * </code>, then a list of paints is used instead (to allow different colors
     * to be used for each series of the radar group).
     *
     * @param paint the paint (<code>null</code> permitted).
     *
     * @see #getSeriesPaint()
     */
public void setSeriesPaint(Paint paint) {
    this.seriesPaint = paint;
    fireChangeEvent();
}
