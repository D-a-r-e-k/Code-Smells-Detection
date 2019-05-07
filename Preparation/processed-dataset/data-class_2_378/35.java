/**
     * Sets the base series paint.
     *
     * @param paint  the paint (<code>null</code> not permitted).
     */
public void setBaseSeriesOutlinePaint(Paint paint) {
    if (paint == null) {
        throw new IllegalArgumentException("Null 'paint' argument.");
    }
    this.baseSeriesOutlinePaint = paint;
    fireChangeEvent();
}
