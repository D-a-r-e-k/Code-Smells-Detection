/**
     * Sets the base series stroke.
     *
     * @param stroke  the stroke (<code>null</code> not permitted).
     */
public void setBaseSeriesOutlineStroke(Stroke stroke) {
    if (stroke == null) {
        throw new IllegalArgumentException("Null 'stroke' argument.");
    }
    this.baseSeriesOutlineStroke = stroke;
    fireChangeEvent();
}
