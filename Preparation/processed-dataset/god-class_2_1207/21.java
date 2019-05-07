/**
     * Sets the paint used to draw the axis lines and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     *
     * @see #getAxisLinePaint()
     * @since 1.0.4
     */
public void setAxisLinePaint(Paint paint) {
    if (paint == null) {
        throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.axisLinePaint = paint;
    fireChangeEvent();
}
