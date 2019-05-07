/**
     * Should allow only limited changes to column type
     */
private void processAlterColumnDataType(Table table, ColumnSchema oldCol) {
    processAlterColumnType(table, oldCol, false);
}
