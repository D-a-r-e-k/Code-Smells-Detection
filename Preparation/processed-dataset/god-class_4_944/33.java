private void addObject(DefaultMutableTreeNode parent, DefaultMutableTreeNode child, boolean forceUpdate, boolean topOfQueue) {
    if (parent == null) {
        parent = root;
    }
    treeModel.insertNodeInto(child, parent, topOfQueue ? 0 : parent.getChildCount());
    if (forceUpdate) {
        tree.setSelectionPath(new TreePath(child.getPath()));
    }
}
