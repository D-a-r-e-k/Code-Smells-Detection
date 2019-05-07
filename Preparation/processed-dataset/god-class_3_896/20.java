/**
     * Export to QIF file.
     */
private void exportQIF() {
    qifFileChooser.setDialogTitle(LANGUAGE.getString("MainFrame.qifExportTitle"));
    qifFileChooser.setFileFilter(qifFileFilter);
    int result = qifFileChooser.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File qifFile = qifFileChooser.getSelectedFile();
        if (dontOverwrite(qifFile))
            return;
        result = accountChooser.showDialog(session.getAccounts(), LANGUAGE.getString("MainFrame.chooseAccountToExport"));
        if (result == OK)
            qif.exportAccount(session, accountChooser.getSelectedAccount(), qifFileChooser.getSelectedFile());
    }
}
