/**
     * Sets the base series paint.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     *
     * @see #getBaseSeriesPaint()
     */
public void setBaseSeriesPaint(Paint paint) {
    if (paint == null) {
        throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.baseSeriesPaint = paint;
    fireChangeEvent();
}
