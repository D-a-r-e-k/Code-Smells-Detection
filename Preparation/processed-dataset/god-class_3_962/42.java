protected String getRecordsDisplayedMessage() {
    FilteredLogTableModel model = _table.getFilteredLogTableModel();
    return getStatusText(model.getRowCount(), model.getTotalRowCount());
}
