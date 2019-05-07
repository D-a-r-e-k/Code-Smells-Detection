/**
     * Sets the shape used for legend items and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param shape  the shape (<code>null</code> not permitted).
     *
     * @see #getLegendItemShape()
     */
public void setLegendItemShape(Shape shape) {
    if (shape == null) {
        throw new IllegalArgumentException("Null 'shape' argument.");
    }
    this.legendItemShape = shape;
    fireChangeEvent();
}
