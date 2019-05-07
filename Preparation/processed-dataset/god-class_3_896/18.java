/**
     * Import QIF file
     */
private void importQIF() {
    qifFileChooser.setDialogTitle(LANGUAGE.getString("MainFrame.import"));
    qifFileChooser.setFileFilter(qifFileFilter);
    int result = qifFileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        navigationTree.clearSelection();
        navigationTreeSelection(null);
        qif.importFile(session, qifFileChooser.getSelectedFile());
        initAccountNode();
        updateNavigationTree();
    }
}
