private Statement compileAlterDomainDropConstraint(Type domain, HsqlName name) {
    String sql = super.getStatement(getParsePosition(), startStatementTokens);
    return new StatementSchema(sql, StatementTypes.ALTER_DOMAIN, null, null);
}
