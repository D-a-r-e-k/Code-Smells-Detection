/**
	 * Whether or not the specified cell is visible.
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 * null is always visible
	 * 
	 * @param cell the whose visibility to determine
	 * @return whether or not the cell is visible
	 */
public boolean isVisible(Object cell) {
    return !isPartial() || visibleSet.contains(cell) || cell == null;
}
