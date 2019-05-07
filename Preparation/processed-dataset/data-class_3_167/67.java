/**
	 * Ungroups all groups in cells and returns the children that are not ports.
	 * Note: This replaces the parents with their group cells in the group
	 * structure.
	 */
public Object[] ungroup(Object[] cells) {
    if (cells != null && cells.length > 0) {
        ArrayList toRemove = new ArrayList();
        ArrayList children = new ArrayList();
        boolean groupExists = false;
        for (int i = 0; i < cells.length; i++) {
            boolean childExists = false;
            ArrayList tempPortList = new ArrayList();
            for (int j = 0; j < getModel().getChildCount(cells[i]); j++) {
                Object child = getModel().getChild(cells[i], j);
                if (!getModel().isPort(child)) {
                    children.add(child);
                    childExists = true;
                } else {
                    tempPortList.add(child);
                }
            }
            if (childExists) {
                toRemove.addAll(tempPortList);
                toRemove.add(cells[i]);
                groupExists = true;
            }
        }
        if (groupExists)
            remove(toRemove.toArray());
        return children.toArray();
    }
    return null;
}
