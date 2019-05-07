/**
	 * Makes the specified cells visible or invisible depending on the flag
	 * passed in. Note the cells really are cells, not cell views.
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 * 
	 * @param cells
	 *            the cells whose visibility is to be changed
	 * @param visible
	 *            <code>true</code> if the cells are to be made visible
	 */
public void setVisible(Object[] cells, boolean visible) {
    if (visible)
        setVisible(cells, null);
    else
        setVisible(null, cells);
}
