private Statement compileAlterColumnSetNullability(Table table, ColumnSchema column, boolean b) {
    String sql = super.getStatement(getParsePosition(), startStatementTokens);
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, null, table.getName());
}
