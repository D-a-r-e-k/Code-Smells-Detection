/**
	 * Makes the specified cell visible or invisible depending on the flag
	 * passed in. Note the cell really is a cell, not a cell view.
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 * 
	 * @param cell
	 *            the cell whose visibility is to be changed
	 * @param visible
	 *            <code>true</code> if cell is to be made visible
	 */
public void setVisible(Object cell, boolean visible) {
    setVisible(new Object[] { cell }, visible);
}
