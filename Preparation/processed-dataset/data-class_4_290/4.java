Statement compileAlter() {
    read();
    switch(token.tokenType) {
        case Tokens.INDEX:
            {
                read();
                HsqlName name = readNewSchemaObjectName(SchemaObject.INDEX, true);
                readThis(Tokens.RENAME);
                readThis(Tokens.TO);
                return compileRenameObject(name, SchemaObject.INDEX);
            }
        case Tokens.SCHEMA:
            {
                read();
                HsqlName name = readSchemaName();
                readThis(Tokens.RENAME);
                readThis(Tokens.TO);
                return compileRenameObject(name, SchemaObject.SCHEMA);
            }
        case Tokens.CATALOG:
            {
                read();
                checkIsSimpleName();
                String name = token.tokenString;
                checkValidCatalogName(name);
                read();
                readThis(Tokens.RENAME);
                readThis(Tokens.TO);
                return compileRenameObject(database.getCatalogName(), SchemaObject.CATALOG);
            }
        case Tokens.SEQUENCE:
            {
                return compileAlterSequence();
            }
        case Tokens.TABLE:
            {
                return compileAlterTable();
            }
        case Tokens.USER:
            {
                return compileAlterUser();
            }
        case Tokens.DOMAIN:
            {
                return compileAlterDomain();
            }
        case Tokens.VIEW:
            {
                return compileCreateView(true);
            }
        default:
            {
                throw unexpectedToken();
            }
    }
}
