/**
     * Sets the outline stroke for ALL series in the plot. If this is set to
     * </code> null</code>, then a list of paints is used instead (to allow
     * different colors to be used for each series).
     *
     * @param stroke  the stroke (<code>null</code> permitted).
     */
public void setSeriesOutlineStroke(Stroke stroke) {
    this.seriesOutlineStroke = stroke;
    fireChangeEvent();
}
