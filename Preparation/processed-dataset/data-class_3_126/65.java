protected void selectAllLogTableColumns(boolean selected) {
    Iterator columns = getLogTableColumns();
    while (columns.hasNext()) {
        getLogTableColumnMenuItem((LogTableColumn) columns.next()).setSelected(selected);
    }
}
