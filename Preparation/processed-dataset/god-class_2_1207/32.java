/**
     * Returns the paint for the specified series.
     *
     * @param series  the series index (zero-based).
     *
     * @return The paint (never <code>null</code>).
     */
public Paint getSeriesOutlinePaint(int series) {
    // return the override, if there is one...  
    if (this.seriesOutlinePaint != null) {
        return this.seriesOutlinePaint;
    }
    // otherwise look up the paint list  
    Paint result = this.seriesOutlinePaintList.getPaint(series);
    if (result == null) {
        result = this.baseSeriesOutlinePaint;
    }
    return result;
}
