private void setSession(Session newSession) {
    session = newSession;
    Collections.sort(session.getAccounts());
    navigator = new NavigationTreeModel();
    initAccountNode();
    navigationTree.setModel(navigator);
    Object[] path = navigator.getPathToRoot(navigator.getAccountNode());
    navigationTree.expandPath(new TreePath(path));
    path = navigator.getPathToRoot(navigator.getReportNode());
    navigationTree.expandPath(new TreePath(path));
    accountPanel.setSession(session);
    categoryPanel.setSession(session);
    menuBar.setSessionOpened(true);
    toolBar.setSessionOpened(true);
}
