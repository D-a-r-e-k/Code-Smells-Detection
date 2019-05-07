/**
     * Auxiliary method to update all UIs of the application
     */
protected void updateUIs() {
    SwingUtilities.updateComponentTreeUI(this);
    SwingUtilities.updateComponentTreeUI(accountPanel);
    SwingUtilities.updateComponentTreeUI(categoryPanel);
    SwingUtilities.updateComponentTreeUI(aboutDialog);
    SwingUtilities.updateComponentTreeUI(optionsDialog);
    SwingUtilities.updateComponentTreeUI(waitDialog);
    SwingUtilities.updateComponentTreeUI(accountChooser);
    SwingUtilities.updateComponentTreeUI(fileChooser);
    SwingUtilities.updateComponentTreeUI(qifFileChooser);
    SwingUtilities.updateComponentTreeUI(mt940FileChooser);
    SwingUtilities.updateComponentTreeUI(accountBalancesReportPanel);
    SwingUtilities.updateComponentTreeUI(incomeExpenseReportPanel);
    navigationTree.setCellRenderer(new NavigationTreeCellRenderer());
}
