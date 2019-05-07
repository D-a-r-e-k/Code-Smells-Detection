protected void selectRow(int foundRow) {
    if (foundRow == -1) {
        String message = _searchText + " not found.";
        JOptionPane.showMessageDialog(_logMonitorFrame, message, "Text not found", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    LF5SwingUtils.selectRow(foundRow, _table, _logTableScrollPane);
}
