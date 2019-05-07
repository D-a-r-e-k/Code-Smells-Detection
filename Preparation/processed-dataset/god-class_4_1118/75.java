private Statement compileAlterDomainAddConstraint(Type domain, Constraint c) {
    String sql = super.getStatement(getParsePosition(), startStatementTokens);
    return new StatementSchema(sql, StatementTypes.ALTER_DOMAIN, null, null);
}
