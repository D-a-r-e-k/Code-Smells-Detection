// This is used to augment the array passed to the setVisible method. 
protected Object[] addVisibleDependencies(Object[] cells, boolean visible) {
    if (cells != null) {
        if (visible) {
            // Make ports and source and target vertex visible 
            Set all = new HashSet();
            for (int i = 0; i < cells.length; i++) {
                all.add(cells[i]);
                // Add ports 
                all.addAll(getPorts(cells[i]));
                // Add source vertex and ports 
                Collection coll = getParentPorts(graphModel.getSource(cells[i]));
                if (coll != null)
                    all.addAll(coll);
                // Add target vertex and ports 
                coll = getParentPorts(graphModel.getTarget(cells[i]));
                if (coll != null)
                    all.addAll(coll);
            }
            if (showsExistingConnections) {
                Set tmp = DefaultGraphModel.getEdges(getModel(), cells);
                Iterator it = tmp.iterator();
                while (it.hasNext()) {
                    Object obj = it.next();
                    Object source = graphModel.getSource(obj);
                    Object target = graphModel.getTarget(obj);
                    if ((isVisible(source) || all.contains(source)) && (isVisible(target) || all.contains(target)))
                        all.add(obj);
                }
            }
            all.removeAll(visibleSet);
            all.remove(null);
            return all.toArray();
        } else {
            if (hidesExistingConnections) {
                Set all = new HashSet();
                for (int i = 0; i < cells.length; i++) {
                    all.addAll(getPorts(cells[i]));
                    all.add(cells[i]);
                }
                Iterator it = DefaultGraphModel.getEdges(graphModel, cells).iterator();
                while (it.hasNext()) {
                    Object edge = it.next();
                    Object newSource = graphModel.getSource(edge);
                    Object newTarget = graphModel.getTarget(edge);
                    // Note: At this time the cells are not yet hidden 
                    if ((newSource != null && !hasVisibleParent(newSource, all)) || (newTarget != null && !hasVisibleParent(newTarget, all))) {
                        all.add(edge);
                    }
                }
                all.remove(null);
                return all.toArray();
            }
        }
    }
    return cells;
}
