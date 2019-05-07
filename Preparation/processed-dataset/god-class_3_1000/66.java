/**
	 * Shows the specified cells with all children if <code>descandants</code>
	 * is true.
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 */
public void showCells(Object[] cells, boolean descandants) {
    if (cells != null && cells.length > 0) {
        if (descandants)
            cells = DefaultGraphModel.getDescendants(getModel(), cells).toArray();
        setVisible(cells, true);
    }
}
