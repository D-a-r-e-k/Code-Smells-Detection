StatementSchema compileCreate() {
    int tableType = TableBase.MEMORY_TABLE;
    boolean isTable = false;
    read();
    switch(token.tokenType) {
        case Tokens.GLOBAL:
            read();
            readThis(Tokens.TEMPORARY);
            readIfThis(Tokens.MEMORY);
            readThis(Tokens.TABLE);
            isTable = true;
            tableType = TableBase.TEMP_TABLE;
            break;
        case Tokens.TEMP:
            read();
            readThis(Tokens.TABLE);
            isTable = true;
            tableType = TableBase.TEMP_TABLE;
            break;
        case Tokens.TEMPORARY:
            read();
            readThis(Tokens.TABLE);
            isTable = true;
            tableType = TableBase.TEMP_TABLE;
            break;
        case Tokens.MEMORY:
            read();
            readThis(Tokens.TABLE);
            isTable = true;
            break;
        case Tokens.CACHED:
            read();
            readThis(Tokens.TABLE);
            isTable = true;
            tableType = TableBase.CACHED_TABLE;
            break;
        case Tokens.TEXT:
            read();
            readThis(Tokens.TABLE);
            isTable = true;
            tableType = TableBase.TEXT_TABLE;
            break;
        case Tokens.TABLE:
            read();
            isTable = true;
            tableType = database.schemaManager.getDefaultTableType();
            break;
        default:
    }
    if (isTable) {
        return compileCreateTable(tableType);
    }
    switch(token.tokenType) {
        // other objects 
        case Tokens.ALIAS:
            return compileCreateAlias();
        case Tokens.SEQUENCE:
            return compileCreateSequence();
        case Tokens.SCHEMA:
            return compileCreateSchema();
        case Tokens.TRIGGER:
            return compileCreateTrigger();
        case Tokens.USER:
            return compileCreateUser();
        case Tokens.ROLE:
            return compileCreateRole();
        case Tokens.VIEW:
            return compileCreateView(false);
        case Tokens.DOMAIN:
            return compileCreateDomain();
        case Tokens.TYPE:
            return compileCreateType();
        case Tokens.CHARACTER:
            return compileCreateCharacterSet();
        // index 
        case Tokens.UNIQUE:
            read();
            checkIsThis(Tokens.INDEX);
            return compileCreateIndex(true);
        case Tokens.INDEX:
            return compileCreateIndex(false);
        case Tokens.AGGREGATE:
        case Tokens.FUNCTION:
        case Tokens.PROCEDURE:
            return compileCreateProcedureOrFunction();
        default:
            {
                throw unexpectedToken();
            }
    }
}
