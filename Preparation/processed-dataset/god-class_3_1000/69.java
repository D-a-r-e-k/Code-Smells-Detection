/**
	 * Collapses all groups by hiding all their descendants.
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 * 
	 * @param groups
	 */
public void collapse(Object[] groups) {
    setCollapsedState(groups, null);
}
