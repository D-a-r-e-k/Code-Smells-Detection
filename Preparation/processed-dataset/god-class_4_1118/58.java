private Statement compileAlterColumnDataType(Table table, ColumnSchema column) {
    HsqlName writeName = null;
    Type typeObject = readTypeDefinition(false);
    String sql = getLastPart();
    Object[] args = new Object[] { table, column, typeObject };
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, null, null, table.getName());
}
