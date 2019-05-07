private void initAccountNode() {
    DefaultMutableTreeNode accountNode = navigator.getAccountNode();
    removeAccountNameListener();
    accountNode.removeAllChildren();
    // add accounts to navigator and install listeners 
    Vector accounts = session.getAccounts();
    for (int i = 0; i < accounts.size(); i++) {
        Account a = (Account) accounts.elementAt(i);
        accountNode.add(new CategoryNode(a));
        a.addPropertyChangeListener(accountNameListener);
    }
}
