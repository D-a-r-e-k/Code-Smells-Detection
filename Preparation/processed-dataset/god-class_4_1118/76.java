private Statement compileAlterDomainSetDefault(Type domain, Expression e) {
    String sql = super.getStatement(getParsePosition(), startStatementTokens);
    return new StatementSchema(sql, StatementTypes.ALTER_DOMAIN, null, null);
}
