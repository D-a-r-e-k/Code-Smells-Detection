StatementSchema compileCreateRole() {
    read();
    HsqlName name = readNewUserIdentifier();
    String sql = getLastPart();
    Object[] args = new Object[] { name };
    return new StatementSchema(sql, StatementTypes.CREATE_ROLE, args);
}
