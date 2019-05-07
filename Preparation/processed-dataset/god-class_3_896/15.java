/**
     * Closes the session.
     */
private void closeSession() {
    if (saveOldSession()) {
        removeAccountNameListener();
        menuBar.setSessionOpened(false);
        toolBar.setSessionOpened(false);
        navigationTree.setModel(null);
        splitPane.add(emptyPanel, JSplitPane.RIGHT);
        splitPane.setDividerLocation(splitPane.getDividerLocation());
        setSessionFile(null);
        session = null;
        navigator = null;
    }
}
