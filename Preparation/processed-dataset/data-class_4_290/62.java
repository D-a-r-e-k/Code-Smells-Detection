private Statement compileAlterColumnSetDefault(Table table, ColumnSchema column, Expression expr) {
    String sql = super.getStatement(getParsePosition(), startStatementTokens);
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, null, table.getName());
}
