protected TreeLayoutNode getTreeLayoutNode(VertexView view, boolean createIfNotPresent) {
    TreeLayoutNode decor = (TreeLayoutNode) view.getAttributes().get(CELL_WRAPPER);
    if (decor == null && createIfNotPresent) {
        TreeLayoutNode n = new TreeLayoutNode(view);
        view.getAttributes().put(CELL_WRAPPER, n);
        return n;
    }
    return decor;
}
