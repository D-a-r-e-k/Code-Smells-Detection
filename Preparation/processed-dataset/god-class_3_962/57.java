protected void showLogLevelColorChangeDialog(JMenuItem result, LogLevel level) {
    JMenuItem menuItem = result;
    Color newColor = JColorChooser.showDialog(_logMonitorFrame, "Choose LogLevel Color", result.getForeground());
    if (newColor != null) {
        // set the color for the record 
        level.setLogLevelColorMap(level, newColor);
        _table.getFilteredLogTableModel().refresh();
    }
}
