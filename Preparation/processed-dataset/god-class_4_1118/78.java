private Statement compileAlterDomainDropDefault(Type domain) {
    String sql = getStatement(getParsePosition(), startStatementTokens);
    return new StatementSchema(sql, StatementTypes.ALTER_DOMAIN, null, null);
}
