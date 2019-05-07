private void updateNavigationTree() {
    TreePath tp = navigationTree.getSelectionPath();
    navigationTree.clearSelection();
    navigationTree.updateUI();
    navigationTree.setSelectionPath(tp);
}
