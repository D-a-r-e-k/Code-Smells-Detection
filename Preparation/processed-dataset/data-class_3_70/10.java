private void navigationTreeSelection(DefaultMutableTreeNode node) {
    if (node == null) {
        splitPane.add(emptyPanel, JSplitPane.RIGHT);
    } else if (node.getParent() == navigator.getAccountNode()) {
        accountPanel.setModel((Account) node.getUserObject());
        splitPane.add(accountPanel, JSplitPane.RIGHT);
    } else if (node == navigator.getCategoryNode()) {
        splitPane.add(categoryPanel, JSplitPane.RIGHT);
    } else if (node == navigator.getBalancesReportNode()) {
        splitPane.add(accountBalancesReportPanel, JSplitPane.RIGHT);
        accountBalancesReportPanel.setSession(session);
        accountBalancesReportPanel.setDateFormat(userProperties.getDateFormat());
    } else if (node == navigator.getIncomeExpenseReportNode()) {
        splitPane.add(incomeExpenseReportPanel, JSplitPane.RIGHT);
        incomeExpenseReportPanel.setSession(session);
        incomeExpenseReportPanel.setDateFormat(userProperties.getDateFormat());
    } else {
        splitPane.add(emptyPanel, JSplitPane.RIGHT);
    }
    splitPane.setDividerLocation(splitPane.getDividerLocation());
}
