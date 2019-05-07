void processAlterTableAddColumn(Table table) {
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
    TableWorks tableWorks = new TableWorks(session, table);
    session.commit(false);
    tableWorks.addColumn(column, colIndex, list);
    return;
}
