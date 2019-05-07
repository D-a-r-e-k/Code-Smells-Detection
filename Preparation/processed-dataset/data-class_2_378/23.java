/**
     * Sets the stroke used to draw the axis lines and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param stroke  the stroke (<code>null</code> not permitted).
     *
     * @see #getAxisLineStroke()
     * @since 1.0.4
     */
public void setAxisLineStroke(Stroke stroke) {
    if (stroke == null) {
        throw new IllegalArgumentException("Null 'stroke' argument.");
    }
    this.axisLineStroke = stroke;
    fireChangeEvent();
}
