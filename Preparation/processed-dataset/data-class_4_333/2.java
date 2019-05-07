/**
     * This function analyses a tree selection event and calls the right methods to take care
     * of building the requested unit's details.
     *
     * @param event The incoming TreeSelectionEvent.
     */
public void valueChanged(TreeSelectionEvent event) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    if (node != null) {
        showDetails((ColopediaTreeItem) node.getUserObject());
    }
}
