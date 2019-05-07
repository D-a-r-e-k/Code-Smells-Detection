/**
     * Returns a collection of legend items for the radar chart.
     *
     * @return The legend items.
     */
public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (getDataset() == null) {
        return result;
    }
    List keys = null;
    if (this.dataExtractOrder == TableOrder.BY_ROW) {
        keys = this.dataset.getRowKeys();
    } else if (this.dataExtractOrder == TableOrder.BY_COLUMN) {
        keys = this.dataset.getColumnKeys();
    }
    if (keys != null) {
        int series = 0;
        Iterator iterator = keys.iterator();
        Shape shape = getLegendItemShape();
        while (iterator.hasNext()) {
            String label = iterator.next().toString();
            String description = label;
            Paint paint = getSeriesPaint(series);
            Paint outlinePaint = getSeriesOutlinePaint(series);
            Stroke stroke = getSeriesOutlineStroke(series);
            LegendItem item = new LegendItem(label, description, null, null, shape, paint, stroke, outlinePaint);
            item.setDataset(getDataset());
            result.add(item);
            series++;
        }
    }
    return result;
}
