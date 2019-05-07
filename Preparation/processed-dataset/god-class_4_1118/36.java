void getCompiledStatementBody(HsqlList list) {
    int position;
    String sql;
    int statementType;
    for (boolean end = false; !end; ) {
        StatementSchema cs = null;
        position = getPosition();
        switch(token.tokenType) {
            case Tokens.CREATE:
                read();
                switch(token.tokenType) {
                    // not in schema definition 
                    case Tokens.SCHEMA:
                    case Tokens.USER:
                    case Tokens.UNIQUE:
                        throw unexpectedToken();
                    case Tokens.INDEX:
                        statementType = StatementTypes.CREATE_INDEX;
                        sql = getStatement(position, startStatementTokensSchema);
                        cs = new StatementSchema(sql, statementType, null);
                        break;
                    case Tokens.SEQUENCE:
                        cs = compileCreateSequence();
                        cs.sql = getLastPart(position);
                        break;
                    case Tokens.ROLE:
                        cs = compileCreateRole();
                        cs.sql = getLastPart(position);
                        break;
                    case Tokens.DOMAIN:
                        statementType = StatementTypes.CREATE_DOMAIN;
                        sql = getStatement(position, startStatementTokensSchema);
                        cs = new StatementSchema(sql, statementType, null);
                        break;
                    case Tokens.TYPE:
                        cs = compileCreateType();
                        cs.sql = getLastPart(position);
                        break;
                    case Tokens.CHARACTER:
                        cs = compileCreateCharacterSet();
                        cs.sql = getLastPart(position);
                        break;
                    // no supported 
                    case Tokens.ASSERTION:
                        throw unexpectedToken();
                    case Tokens.TABLE:
                    case Tokens.MEMORY:
                    case Tokens.CACHED:
                    case Tokens.TEMP:
                    case Tokens.GLOBAL:
                    case Tokens.TEMPORARY:
                    case Tokens.TEXT:
                        statementType = StatementTypes.CREATE_TABLE;
                        sql = getStatement(position, startStatementTokensSchema);
                        cs = new StatementSchema(sql, statementType, null);
                        break;
                    case Tokens.TRIGGER:
                        statementType = StatementTypes.CREATE_TRIGGER;
                        sql = getStatement(position, startStatementTokensSchema);
                        cs = new StatementSchema(sql, statementType, null);
                        break;
                    case Tokens.VIEW:
                        statementType = StatementTypes.CREATE_VIEW;
                        sql = getStatement(position, startStatementTokensSchema);
                        cs = new StatementSchema(sql, statementType, null);
                        break;
                    case Tokens.FUNCTION:
                        statementType = StatementTypes.CREATE_ROUTINE;
                        sql = getStatementForRoutine(position, startStatementTokensSchema);
                        cs = new StatementSchema(sql, statementType, null);
                        break;
                    case Tokens.PROCEDURE:
                        statementType = StatementTypes.CREATE_ROUTINE;
                        sql = getStatementForRoutine(position, startStatementTokensSchema);
                        cs = new StatementSchema(sql, statementType, null);
                        break;
                    default:
                        throw unexpectedToken();
                }
                break;
            case Tokens.GRANT:
                cs = compileGrantOrRevoke();
                cs.sql = getLastPart(position);
                break;
            case Tokens.SEMICOLON:
                read();
                end = true;
                break;
            case Tokens.X_ENDPARSE:
                end = true;
                break;
            default:
                throw unexpectedToken();
        }
        if (cs != null) {
            cs.isSchemaDefinition = true;
            list.add(cs);
        }
    }
}
