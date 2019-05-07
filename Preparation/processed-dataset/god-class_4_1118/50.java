Statement compileAlterTableAddColumn(Table table) {
    int colIndex = table.getColumnCount();
    HsqlArrayList list = new HsqlArrayList();
    Constraint constraint = new Constraint(null, null, SchemaObject.ConstraintTypes.TEMP);
    list.add(constraint);
    checkIsSchemaObjectName();
    HsqlName hsqlName = database.nameManager.newColumnHsqlName(table.getName(), token.tokenString, isDelimitedIdentifier());
    read();
    ColumnSchema column = readColumnDefinitionOrNull(table, hsqlName, list);
    if (column == null) {
        throw Error.error(ErrorCode.X_42000);
    }
    if (token.tokenType == Tokens.BEFORE) {
        read();
        colIndex = table.getColumnIndex(token.tokenString);
        read();
    }
    String sql = getLastPart();
    Object[] args = new Object[] { column, new Integer(colIndex), list };
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, args, null, table.getName());
}
