/**
     * Creates a new account.
     */
private void newAccount() {
    Account account = session.getNewAccount(LANGUAGE.getString("Account.newAccount"));
    Collections.sort(session.getAccounts());
    CategoryNode node = new CategoryNode(account);
    account.addPropertyChangeListener(accountNameListener);
    navigator.insertNodeInto(node, navigator.getAccountNode(), 0);
    Object path[] = navigator.getPathToRoot(node);
    navigationTree.setSelectionPath(new TreePath(path));
    accountPanel.tabbedPane.setSelectedComponent(accountPanel.propertiesPanel);
}
