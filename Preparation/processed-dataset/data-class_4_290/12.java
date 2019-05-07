StatementSchema compileCreateTable(int type) {
    HsqlName name = readNewSchemaObjectName(SchemaObject.TABLE, false);
    name.setSchemaIfNull(session.getCurrentSchemaHsqlName());
    Table table = TableUtil.newTable(database, type, name);
    return compileCreateTableBody(table);
}
