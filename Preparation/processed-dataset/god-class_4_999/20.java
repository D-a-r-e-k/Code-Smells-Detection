/**
	 * Draws the edge labels
	 * @param g the graphics object being painted to
	 */
protected void paintLabels(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setStroke(new BasicStroke(1));
    g.setFont((extraLabelFont != null) ? extraLabelFont : getFont());
    Object[] labels = GraphConstants.getExtraLabels(view.getAllAttributes());
    JGraph graph = (JGraph) this.graph.get();
    if (labels != null) {
        for (int i = 0; i < labels.length; i++) paintLabel(g, graph.convertValueToString(labels[i]), getExtraLabelPosition(view, i), false || !simpleExtraLabels);
    }
    if (graph.getEditingCell() != view.getCell()) {
        g.setFont(getFont());
        Object label = graph.convertValueToString(view);
        if (label != null) {
            paintLabel(g, label.toString(), getLabelPosition(view), true);
        }
    }
}
