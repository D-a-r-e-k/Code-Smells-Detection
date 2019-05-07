/**
     * loop through each of the series to get the maximum value
     * on each category axis
     *
     * @param seriesCount  the number of series
     * @param catCount  the number of categories
     */
private void calculateMaxValue(int seriesCount, int catCount) {
    double v = 0;
    Number nV = null;
    for (int seriesIndex = 0; seriesIndex < seriesCount; seriesIndex++) {
        for (int catIndex = 0; catIndex < catCount; catIndex++) {
            nV = getPlotValue(seriesIndex, catIndex);
            if (nV != null) {
                v = nV.doubleValue();
                if (v > this.maxValue) {
                    this.maxValue = v;
                }
            }
        }
    }
}
