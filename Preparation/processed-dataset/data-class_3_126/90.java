protected void setLeastSevereDisplayedLogLevel(LogLevel level) {
    if (level == null || _leastSevereDisplayedLogLevel == level) {
        return;
    }
    _leastSevereDisplayedLogLevel = level;
    _table.getFilteredLogTableModel().refresh();
    updateStatusLabel();
}
