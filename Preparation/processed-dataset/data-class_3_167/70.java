/**
	 * Expands all groups by showing all children. (Note: This does not show all
	 * descandants, but only the first generation of children.)
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 */
public void expand(Object[] cells) {
    setCollapsedState(null, cells);
}
