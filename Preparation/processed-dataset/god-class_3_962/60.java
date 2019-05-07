protected JCheckBoxMenuItem getLogTableColumnMenuItem(LogTableColumn column) {
    JCheckBoxMenuItem result = (JCheckBoxMenuItem) (_logTableColumnMenuItems.get(column));
    if (result == null) {
        result = createLogTableColumnMenuItem(column);
        _logTableColumnMenuItems.put(column, result);
    }
    return result;
}
