Statement compileAlterTableDropColumn(Table table, String colName, boolean cascade) {
    HsqlName writeName = null;
    int colindex = table.getColumnIndex(colName);
    if (table.getColumnCount() == 1) {
        throw Error.error(ErrorCode.X_42591);
    }
    Object[] args = new Object[] { table.getColumn(colindex).getName(), ValuePool.getInt(SchemaObject.CONSTRAINT), Boolean.valueOf(cascade), Boolean.valueOf(false) };
    if (!table.isTemp()) {
        writeName = table.getName();
    }
    return new StatementSchema(null, StatementTypes.DROP_COLUMN, args, null, writeName);
}
