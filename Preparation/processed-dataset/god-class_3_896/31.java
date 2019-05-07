private void accountNameChanged() {
    Collections.sort(session.getAccounts());
    TreePath tp = navigationTree.getSelectionPath();
    navigator.sortChildren(navigator.getAccountNode());
    navigationTree.setSelectionPath(tp);
}
