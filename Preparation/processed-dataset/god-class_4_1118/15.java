StatementSchema readTableAsSubqueryDefinition(Table table) {
    HsqlName readName = null;
    boolean withData = true;
    HsqlName[] columnNames = null;
    Statement statement = null;
    if (token.tokenType == Tokens.OPENBRACKET) {
        columnNames = readColumnNames(table.getName());
    }
    readThis(Tokens.AS);
    readThis(Tokens.OPENBRACKET);
    QueryExpression queryExpression = XreadQueryExpression();
    queryExpression.setReturningResult();
    queryExpression.resolve(session);
    readThis(Tokens.CLOSEBRACKET);
    readThis(Tokens.WITH);
    if (token.tokenType == Tokens.NO) {
        read();
        withData = false;
    } else if (table.getTableType() == TableBase.TEXT_TABLE) {
        throw unexpectedTokenRequire(Tokens.T_NO);
    }
    readThis(Tokens.DATA);
    if (token.tokenType == Tokens.ON) {
        if (!table.isTemp()) {
            throw unexpectedToken();
        }
        read();
        readThis(Tokens.COMMIT);
        if (token.tokenType == Tokens.DELETE) {
        } else if (token.tokenType == Tokens.PRESERVE) {
            table.persistenceScope = TableBase.SCOPE_SESSION;
        }
        read();
        readThis(Tokens.ROWS);
    }
    if (columnNames == null) {
        columnNames = queryExpression.getResultColumnNames();
    } else {
        if (columnNames.length != queryExpression.getColumnCount()) {
            throw Error.error(ErrorCode.X_42593);
        }
    }
    TableUtil.setColumnsInSchemaTable(table, columnNames, queryExpression.getColumnTypes());
    table.createPrimaryKey();
    if (withData) {
        statement = new StatementQuery(session, queryExpression, compileContext);
        readName = statement.getTableNamesForRead()[0];
    }
    Object[] args = new Object[] { table, null, statement };
    String sql = getLastPart();
    StatementSchema st = new StatementSchema(sql, StatementTypes.CREATE_TABLE, args, readName, null);
    return st;
}
