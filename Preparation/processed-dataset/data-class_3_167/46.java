/**
	 * Changes the visibility state of the cells passed in. Note that the arrays
	 * must contain cells, not cell views.
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 *
	 * @param visible
	 *            cells to be made visible
	 * @param invisible
	 *            cells to be made invisible
	 * @param cs
	 *            a <code>ConnectionSet</code> describing the new state of
	 *            edge connections in the graph
	 */
public void setVisible(Object[] visible, Object[] invisible, ConnectionSet cs) {
    setVisible(visible, invisible, null, cs);
}
