/**
	 * Updates the cached array of ports.
	 */
protected void updatePorts() {
    Object[] roots = DefaultGraphModel.getRoots(graphModel);
    List list = DefaultGraphModel.getDescendants(graphModel, roots);
    if (list != null) {
        ArrayList result = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Object cell = it.next();
            if (graphModel.isPort(cell)) {
                CellView portView = getMapping(cell, false);
                if (portView != null) {
                    result.add(portView);
                    portView.refresh(this, this, false);
                }
            }
        }
        ports = new PortView[result.size()];
        result.toArray(ports);
    }
}
