StatementSchema compileCreateView(boolean alter) {
    read();
    HsqlName name = readNewSchemaObjectName(SchemaObject.VIEW, true);
    name.setSchemaIfNull(session.getCurrentSchemaHsqlName());
    checkSchemaUpdateAuthorisation(name.schema);
    HsqlName[] colList = null;
    if (token.tokenType == Tokens.OPENBRACKET) {
        colList = readColumnNames(name);
    }
    readThis(Tokens.AS);
    startRecording();
    int position = getPosition();
    QueryExpression queryExpression;
    try {
        queryExpression = XreadQueryExpression();
    } catch (HsqlException e) {
        queryExpression = XreadJoinedTable();
    }
    Token[] tokenisedStatement = getRecordedStatement();
    int check = SchemaObject.ViewCheckModes.CHECK_NONE;
    if (token.tokenType == Tokens.WITH) {
        read();
        check = SchemaObject.ViewCheckModes.CHECK_CASCADE;
        if (readIfThis(Tokens.LOCAL)) {
            check = SchemaObject.ViewCheckModes.CHECK_LOCAL;
        } else {
            readIfThis(Tokens.CASCADED);
        }
        readThis(Tokens.CHECK);
        readThis(Tokens.OPTION);
    }
    View view = new View(database, name, colList, check);
    queryExpression.setView(view);
    queryExpression.resolve(session);
    view.setStatement(Token.getSQL(tokenisedStatement));
    String fullSQL = getLastPart();
    Object[] args = new Object[] { view };
    int type = alter ? StatementTypes.ALTER_VIEW : StatementTypes.CREATE_VIEW;
    StatementSchema cs = new StatementSchema(fullSQL, type, args);
    StatementQuery s = new StatementQuery(session, queryExpression, compileContext);
    cs.readTableNames = s.readTableNames;
    return cs;
}
