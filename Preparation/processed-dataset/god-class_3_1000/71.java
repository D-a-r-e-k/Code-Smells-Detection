/**
	 * Collapses and/or expands the specified cell(s)
	 * NOTE: Your GraphLayoutCache must be <code>partial</code> (set 
	 * <code>partial</code> to <code>true</code> in the constructor)
	 * in order to use the visibility functionality of expand/collapse,
	 * setVisible, etc.
	 * 
	 * @param collapse
	 *            the cells to be collapsed
	 * @param expand
	 *            the cells to be expanded
	 */
public void setCollapsedState(Object[] collapse, Object[] expand) {
    // Get all descandants for the groups 
    ConnectionSet cs = new ConnectionSet();
    // Collapse cells 
    List toHide = DefaultGraphModel.getDescendants(getModel(), collapse);
    if (collapse != null) {
        // Remove the groups themselfes 
        for (int i = 0; i < collapse.length; i++) {
            toHide.remove(collapse[i]);
            cellWillCollapse(collapse[i]);
        }
        // Remove the ports (will be hidden automatically) 
        for (int i = 0; i < collapse.length; i++) {
            int childCount = getModel().getChildCount(collapse[i]);
            if (childCount > 0) {
                for (int j = 0; j < childCount; j++) {
                    Object child = getModel().getChild(collapse[i], j);
                    if (getModel().isPort(child)) {
                        toHide.remove(child);
                    }
                }
            }
        }
    }
    // Expand cells 
    Set toShow = new HashSet();
    if (expand != null) {
        for (int i = 0; i < expand.length; i++) {
            int childCount = getModel().getChildCount(expand[i]);
            for (int j = 0; j < childCount; j++) {
                toShow.add(getModel().getChild(expand[i], j));
            }
        }
    }
    setVisible(toShow.toArray(), (toHide != null) ? toHide.toArray() : null, cs);
}
