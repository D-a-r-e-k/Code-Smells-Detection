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
	 */
public void setVisible(Object[] visible, Object[] invisible) {
    setVisible(visible, invisible, null);
}
