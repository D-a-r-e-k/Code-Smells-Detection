protected void findSearchText() {
    String text = _searchText;
    if (text == null || text.length() == 0) {
        return;
    }
    int startRow = getFirstSelectedRow();
    int foundRow = findRecord(startRow, text, _table.getFilteredLogTableModel().getFilteredRecords());
    selectRow(foundRow);
}
