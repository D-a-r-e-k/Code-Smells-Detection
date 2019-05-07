/**
     * Returns the value to be plotted at the interseries of the
     * series and the category.  This allows us to plot
     * <code>BY_ROW</code> or <code>BY_COLUMN</code> which basically is just
     * reversing the definition of the categories and data series being
     * plotted.
     *
     * @param series the series to be plotted.
     * @param cat the category within the series to be plotted.
     *
     * @return The value to be plotted (possibly <code>null</code>).
     *
     * @see #getDataExtractOrder()
     */
protected Number getPlotValue(int series, int cat) {
    Number value = null;
    if (this.dataExtractOrder == TableOrder.BY_ROW) {
        value = this.dataset.getValue(series, cat);
    } else if (this.dataExtractOrder == TableOrder.BY_COLUMN) {
        value = this.dataset.getValue(cat, series);
    }
    return value;
}
