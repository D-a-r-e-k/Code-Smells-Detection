/**
     * Removes an existing account.
     */
private void deleteAccount() {
    CategoryNode node = (CategoryNode) navigationTree.getSelectionPath().getLastPathComponent();
    Account account = (Account) node.getUserObject();
    session.getCategories().removeNodeFromParent(account.getCategoryNode());
    session.getAccounts().removeElement(account);
    navigator.removeNodeFromParent(node);
    session.modified();
}
