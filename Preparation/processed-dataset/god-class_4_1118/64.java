private Statement compileAlterColumnDropDefault(Table table, ColumnSchema column, int columnIndex) {
    String sql = super.getStatement(getParsePosition(), startStatementTokens);
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, null, table.getName());
}
