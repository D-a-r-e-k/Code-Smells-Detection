Statement compileAlterUser() {
    read();
    String password;
    User userObject;
    HsqlName userName = readNewUserIdentifier();
    userObject = database.getUserManager().get(userName.name);
    if (userName.name.equals(Tokens.T_PUBLIC)) {
        throw Error.error(ErrorCode.X_42503);
    }
    readThis(Tokens.SET);
    if (token.tokenType == Tokens.PASSWORD) {
        read();
        password = readPassword();
        Object[] args = new Object[] { userObject, password };
        return new StatementCommand(StatementTypes.SET_USER_PASSWORD, args);
    } else if (token.tokenType == Tokens.INITIAL) {
        read();
        readThis(Tokens.SCHEMA);
        HsqlName schemaName;
        if (token.tokenType == Tokens.DEFAULT) {
            schemaName = null;
        } else {
            schemaName = database.schemaManager.getSchemaHsqlName(token.tokenString);
        }
        read();
        Object[] args = new Object[] { userObject, schemaName };
        return new StatementCommand(StatementTypes.SET_USER_INITIAL_SCHEMA, args);
    } else {
        throw unexpectedToken();
    }
}
