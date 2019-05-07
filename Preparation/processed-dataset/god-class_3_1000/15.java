/**
	 * Remaps all existing views using the CellViewFactory
	 * and replaces the respective root views.
	 */
public synchronized void reload() {
    List newRoots = new ArrayList();
    Map oldMapping = new Hashtable(mapping);
    mapping.clear();
    Iterator it = oldMapping.keySet().iterator();
    Set rootsSet = new HashSet(roots);
    while (it.hasNext()) {
        Object cell = it.next();
        CellView oldView = (CellView) oldMapping.get(cell);
        CellView newView = getMapping(cell, true);
        newView.changeAttributes(this, oldView.getAttributes());
        // newView.refresh(getModel(), this, false); 
        if (rootsSet.contains(oldView))
            newRoots.add(newView);
    }
    // replace hidden 
    hiddenMapping.clear();
    roots = newRoots;
}
