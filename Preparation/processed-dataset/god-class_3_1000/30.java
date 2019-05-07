/**
	 * Checks if the port or one of its parents is visible.
	 */
protected boolean hasVisibleParent(Object cell, Set invisible) {
    boolean isVisible = false;
    do {
        isVisible = (invisible == null || !invisible.contains(cell)) ? isVisible(cell) : false;
        cell = getModel().getParent(cell);
    } while (cell != null && !isVisible);
    return isVisible;
}
