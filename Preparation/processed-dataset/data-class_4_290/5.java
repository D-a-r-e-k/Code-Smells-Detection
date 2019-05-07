/*
    CompiledStatementInterface compileAlter() {

        CompiledStatementInterface cs = null;
        read();
        String sql = getStatement(getParsePosition(), endStatementTokensAlter);

        cs = new CompiledStatementSchema(sql, StatementCodes.ALTER_TYPE, null);

        return cs;
    }
*/
Statement compileDrop() {
    int objectTokenType;
    int objectType;
    int statementType;
    boolean canCascade = false;
    boolean cascade = false;
    boolean useIfExists = false;
    boolean ifExists = false;
    HsqlName writeName = null;
    HsqlName catalogName = database.getCatalogName();
    read();
    objectTokenType = this.token.tokenType;
    switch(objectTokenType) {
        case Tokens.INDEX:
            {
                read();
                statementType = StatementTypes.DROP_INDEX;
                objectType = SchemaObject.INDEX;
                useIfExists = true;
                writeName = catalogName;
                break;
            }
        case Tokens.ASSERTION:
            {
                read();
                statementType = StatementTypes.DROP_ASSERTION;
                objectType = SchemaObject.ASSERTION;
                canCascade = true;
                break;
            }
        case Tokens.SPECIFIC:
            {
                read();
                switch(token.tokenType) {
                    case Tokens.ROUTINE:
                    case Tokens.PROCEDURE:
                    case Tokens.FUNCTION:
                        read();
                        break;
                    default:
                        throw unexpectedToken();
                }
                statementType = StatementTypes.DROP_ROUTINE;
                objectType = SchemaObject.SPECIFIC_ROUTINE;
                writeName = catalogName;
                canCascade = true;
                useIfExists = true;
                break;
            }
        case Tokens.PROCEDURE:
            {
                read();
                statementType = StatementTypes.DROP_ROUTINE;
                objectType = SchemaObject.PROCEDURE;
                writeName = catalogName;
                canCascade = true;
                useIfExists = true;
                break;
            }
        case Tokens.FUNCTION:
            {
                read();
                statementType = StatementTypes.DROP_ROUTINE;
                objectType = SchemaObject.FUNCTION;
                writeName = catalogName;
                canCascade = true;
                useIfExists = true;
                break;
            }
        case Tokens.SCHEMA:
            {
                read();
                statementType = StatementTypes.DROP_SCHEMA;
                objectType = SchemaObject.SCHEMA;
                writeName = catalogName;
                canCascade = true;
                useIfExists = true;
                break;
            }
        case Tokens.SEQUENCE:
            {
                read();
                statementType = StatementTypes.DROP_SEQUENCE;
                objectType = SchemaObject.SEQUENCE;
                writeName = catalogName;
                canCascade = true;
                useIfExists = true;
                break;
            }
        case Tokens.TRIGGER:
            {
                read();
                statementType = StatementTypes.DROP_TRIGGER;
                objectType = SchemaObject.TRIGGER;
                writeName = catalogName;
                canCascade = false;
                useIfExists = true;
                break;
            }
        case Tokens.USER:
            {
                read();
                statementType = StatementTypes.DROP_USER;
                objectType = SchemaObject.GRANTEE;
                writeName = catalogName;
                canCascade = true;
                break;
            }
        case Tokens.ROLE:
            {
                read();
                statementType = StatementTypes.DROP_ROLE;
                objectType = SchemaObject.GRANTEE;
                writeName = catalogName;
                canCascade = true;
                break;
            }
        case Tokens.DOMAIN:
            read();
            statementType = StatementTypes.DROP_DOMAIN;
            objectType = SchemaObject.DOMAIN;
            writeName = catalogName;
            canCascade = true;
            useIfExists = true;
            break;
        case Tokens.TYPE:
            read();
            statementType = StatementTypes.DROP_TYPE;
            objectType = SchemaObject.TYPE;
            writeName = catalogName;
            canCascade = true;
            useIfExists = true;
            break;
        case Tokens.CHARACTER:
            read();
            readThis(Tokens.SET);
            statementType = StatementTypes.DROP_CHARACTER_SET;
            objectType = SchemaObject.CHARSET;
            writeName = catalogName;
            canCascade = false;
            useIfExists = true;
            break;
        case Tokens.VIEW:
            read();
            statementType = StatementTypes.DROP_VIEW;
            objectType = SchemaObject.VIEW;
            writeName = catalogName;
            canCascade = true;
            useIfExists = true;
            break;
        case Tokens.TABLE:
            read();
            statementType = StatementTypes.DROP_TABLE;
            objectType = SchemaObject.TABLE;
            writeName = catalogName;
            canCascade = true;
            useIfExists = true;
            break;
        default:
            throw unexpectedToken();
    }
    if (useIfExists && token.tokenType == Tokens.IF) {
        int position = getPosition();
        read();
        if (token.tokenType == Tokens.EXISTS) {
            read();
            ifExists = true;
        } else {
            rewind(position);
        }
    }
    checkIsIdentifier();
    HsqlName name = null;
    switch(objectTokenType) {
        case Tokens.USER:
            {
                checkIsSimpleName();
                checkDatabaseUpdateAuthorisation();
                Grantee grantee = database.getUserManager().get(token.tokenString);
                name = grantee.getName();
                read();
                break;
            }
        case Tokens.ROLE:
            {
                checkIsSimpleName();
                checkDatabaseUpdateAuthorisation();
                Grantee role = database.getGranteeManager().getRole(token.tokenString);
                name = role.getName();
                read();
                break;
            }
        case Tokens.SCHEMA:
            {
                name = readNewSchemaName();
                writeName = catalogName;
                break;
            }
        case Tokens.TABLE:
            {
                boolean isModule = token.namePrePrefix == null && Tokens.T_MODULE.equals(token.namePrefix);
                name = readNewSchemaObjectName(objectType, false);
                if (isModule) {
                    Object[] args = new Object[] { name, Boolean.valueOf(ifExists) };
                    return new StatementSession(StatementTypes.DROP_TABLE, args);
                }
                break;
            }
        default:
            name = readNewSchemaObjectName(objectType, false);
    }
    if (!ifExists && useIfExists && token.tokenType == Tokens.IF) {
        read();
        readThis(Tokens.EXISTS);
        ifExists = true;
    }
    if (canCascade) {
        if (token.tokenType == Tokens.CASCADE) {
            cascade = true;
            read();
        } else if (token.tokenType == Tokens.RESTRICT) {
            read();
        }
    }
    Object[] args = new Object[] { name, new Integer(objectType), Boolean.valueOf(cascade), Boolean.valueOf(ifExists) };
    String sql = getLastPart();
    Statement cs = new StatementSchema(sql, statementType, args, null, writeName);
    return cs;
}
