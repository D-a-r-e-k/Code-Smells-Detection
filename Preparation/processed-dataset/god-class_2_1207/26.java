/**
     * Returns the paint for the specified series.
     *
     * @param series  the series index (zero-based).
     *
     * @return The paint (never <code>null</code>).
     *
     * @see #setSeriesPaint(int, Paint)
     */
public Paint getSeriesPaint(int series) {
    // return the override, if there is one...  
    if (this.seriesPaint != null) {
        return this.seriesPaint;
    }
    // otherwise look up the paint list  
    Paint result = this.seriesPaintList.getPaint(series);
    if (result == null) {
        DrawingSupplier supplier = getDrawingSupplier();
        if (supplier != null) {
            Paint p = supplier.getNextPaint();
            this.seriesPaintList.setPaint(series, p);
            result = p;
        } else {
            result = this.baseSeriesPaint;
        }
    }
    return result;
}
