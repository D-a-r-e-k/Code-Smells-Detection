StatementSchema compileCreateIndex(boolean unique) {
    Table table;
    HsqlName indexHsqlName;
    read();
    indexHsqlName = readNewSchemaObjectName(SchemaObject.INDEX, true);
    readThis(Tokens.ON);
    table = readTableName();
    HsqlName tableSchema = table.getSchemaName();
    indexHsqlName.setSchemaIfNull(tableSchema);
    indexHsqlName.parent = table.getName();
    if (indexHsqlName.schema != tableSchema) {
        throw Error.error(ErrorCode.X_42505);
    }
    indexHsqlName.schema = table.getSchemaName();
    int[] indexColumns = readColumnList(table, true);
    String sql = getLastPart();
    Object[] args = new Object[] { table, indexColumns, indexHsqlName, Boolean.valueOf(unique) };
    return new StatementSchema(sql, StatementTypes.CREATE_INDEX, args, null, table.getName());
}
