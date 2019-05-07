/**
	 * Returns the label size of the specified view in the given graph.
	 */
public Dimension getExtraLabelSize(JGraph paintingContext, EdgeView view, int index) {
    Object[] labels = GraphConstants.getExtraLabels(view.getAllAttributes());
    if (labels != null && index < labels.length) {
        String label = (paintingContext != null) ? paintingContext.convertValueToString(labels[index]) : String.valueOf(labels[index]);
        return getLabelSize(view, label);
    }
    return null;
}
