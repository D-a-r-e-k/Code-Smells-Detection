private void removeAccountNameListener() {
    for (Enumeration e = navigator.getAccountNode().children(); e.hasMoreElements(); ) {
        Account a = (Account) ((CategoryNode) e.nextElement()).getUserObject();
        a.removePropertyChangeListener(accountNameListener);
    }
}
