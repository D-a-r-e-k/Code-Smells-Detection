/**
	 * Installs the attributes of specified cell in this renderer instance. This
	 * means, retrieve every published key from the cells hashtable and set
	 * global variables or superclass properties accordingly.
	 * 
	 * @param view
	 *            the cell view to retrieve the attribute values from.
	 */
protected void installAttributes(CellView view) {
    Map map = view.getAllAttributes();
    beginDeco = GraphConstants.getLineBegin(map);
    beginSize = GraphConstants.getBeginSize(map);
    beginFill = GraphConstants.isBeginFill(map) && isFillable(beginDeco);
    endDeco = GraphConstants.getLineEnd(map);
    endSize = GraphConstants.getEndSize(map);
    endFill = GraphConstants.isEndFill(map) && isFillable(endDeco);
    lineWidth = GraphConstants.getLineWidth(map);
    Edge.Routing routing = GraphConstants.getRouting(map);
    lineStyle = (routing != null && view instanceof EdgeView) ? routing.getPreferredLineStyle((EdgeView) view) : Edge.Routing.NO_PREFERENCE;
    if (lineStyle == Edge.Routing.NO_PREFERENCE)
        lineStyle = GraphConstants.getLineStyle(map);
    lineDash = GraphConstants.getDashPattern(map);
    dashOffset = GraphConstants.getDashOffset(map);
    borderColor = GraphConstants.getBorderColor(map);
    Color foreground = GraphConstants.getLineColor(map);
    setForeground((foreground != null) ? foreground : defaultForeground);
    Color background = GraphConstants.getBackground(map);
    setBackground((background != null) ? background : defaultBackground);
    Color gradientColor = GraphConstants.getGradientColor(map);
    setGradientColor(gradientColor);
    setOpaque(GraphConstants.isOpaque(map));
    setFont(GraphConstants.getFont(map));
    Color tmp = GraphConstants.getForeground(map);
    fontColor = (tmp != null) ? tmp : getForeground();
    labelTransformEnabled = GraphConstants.isLabelAlongEdge(map);
    labelsEnabled = GraphConstants.isLabelEnabled(map);
}
