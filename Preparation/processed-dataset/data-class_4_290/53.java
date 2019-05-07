/**
     * Responsible for handling tail of ALTER TABLE ... DROP COLUMN ...
     */
void processAlterTableDropColumn(Table table, String colName, boolean cascade) {
    int colindex = table.getColumnIndex(colName);
    if (table.getColumnCount() == 1) {
        throw Error.error(ErrorCode.X_42591);
    }
    session.commit(false);
    TableWorks tableWorks = new TableWorks(session, table);
    tableWorks.dropColumn(colindex, cascade);
}
