StatementSchema compileCreateUser() {
    HsqlName name;
    String password;
    boolean admin = false;
    Grantee grantor = session.getGrantee();
    read();
    name = readNewUserIdentifier();
    readThis(Tokens.PASSWORD);
    password = readPassword();
    if (token.tokenType == Tokens.ADMIN) {
        read();
        admin = true;
    }
    checkDatabaseUpdateAuthorisation();
    String sql = getLastPart();
    Object[] args = new Object[] { name, password, grantor, Boolean.valueOf(admin) };
    return new StatementSchema(sql, StatementTypes.CREATE_USER, args);
}
