/**
     * Sets the paint used to fill a series of the radar and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero-based).
     * @param paint  the paint (<code>null</code> permitted).
     */
public void setSeriesOutlinePaint(int series, Paint paint) {
    this.seriesOutlinePaintList.setPaint(series, paint);
    fireChangeEvent();
}
