/**
     * Returns the stroke for the specified series.
     *
     * @param series  the series index (zero-based).
     *
     * @return The stroke (never <code>null</code>).
     */
public Stroke getSeriesOutlineStroke(int series) {
    // return the override, if there is one...  
    if (this.seriesOutlineStroke != null) {
        return this.seriesOutlineStroke;
    }
    // otherwise look up the paint list  
    Stroke result = this.seriesOutlineStrokeList.getStroke(series);
    if (result == null) {
        result = this.baseSeriesOutlineStroke;
    }
    return result;
}
