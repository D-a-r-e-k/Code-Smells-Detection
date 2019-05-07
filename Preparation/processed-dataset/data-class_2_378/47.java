/**
     * Sets the series label paint and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     *
     * @see #getLabelPaint()
     */
public void setLabelPaint(Paint paint) {
    if (paint == null) {
        throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.labelPaint = paint;
    fireChangeEvent();
}
