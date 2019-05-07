private void select(String id) {
    DefaultMutableTreeNode node = nodeMap.get(id);
    if (node == null) {
        logger.warning("Unable to find node with id '" + id + "'.");
    } else {
        TreePath oldPath = tree.getSelectionPath();
        if (oldPath != null && oldPath.getParentPath() != null) {
            tree.collapsePath(oldPath.getParentPath());
        }
        TreePath newPath = new TreePath(node.getPath());
        tree.scrollPathToVisible(newPath);
        tree.expandPath(newPath);
        showDetails((ColopediaTreeItem) node.getUserObject());
    }
}
