private Statement compileAlterColumnType(Table table, ColumnSchema column) {
    String sql = super.getStatement(getParsePosition(), startStatementTokens);
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, null, table.getName());
}
