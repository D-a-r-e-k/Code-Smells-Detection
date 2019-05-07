/**
     * Sets the paint used to fill a series of the radar and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param paint  the paint (<code>null</code> permitted).
     *
     * @see #getSeriesPaint(int)
     */
public void setSeriesPaint(int series, Paint paint) {
    this.seriesPaintList.setPaint(series, paint);
    fireChangeEvent();
}
