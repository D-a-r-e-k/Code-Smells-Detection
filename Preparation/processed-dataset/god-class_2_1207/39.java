/**
     * Sets the stroke used to fill a series of the radar and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param stroke  the stroke (<code>null</code> permitted).
     */
public void setSeriesOutlineStroke(int series, Stroke stroke) {
    this.seriesOutlineStrokeList.setStroke(series, stroke);
    fireChangeEvent();
}
