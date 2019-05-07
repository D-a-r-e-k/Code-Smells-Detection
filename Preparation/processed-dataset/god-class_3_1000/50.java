protected Collection getParentPorts(Object cell) {
    // does nothing if a parent is already visible 
    Object parent = graphModel.getParent(cell);
    while (parent != null) {
        if (isVisible(parent))
            return null;
        parent = graphModel.getParent(parent);
    }
    // Else returns the parent and all ports 
    parent = graphModel.getParent(cell);
    Collection collection = getPorts(parent);
    collection.add(parent);
    return collection;
}
