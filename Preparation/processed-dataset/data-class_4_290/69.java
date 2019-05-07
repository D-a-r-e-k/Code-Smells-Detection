/**
     * Responsible for handling tail of ALTER COLUMN ... RENAME ...
     */
private void processAlterColumnRename(Table table, ColumnSchema column) {
    checkIsSimpleName();
    if (table.findColumn(token.tokenString) > -1) {
        throw Error.error(ErrorCode.X_42504, token.tokenString);
    }
    database.schemaManager.checkColumnIsReferenced(table.getName(), column.getName());
    session.commit(false);
    table.renameColumn(column, token.tokenString, isDelimitedIdentifier());
    read();
}
