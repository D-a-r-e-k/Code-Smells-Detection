StatementSchema compileComment() {
    HsqlName name;
    int type;
    readThis(Tokens.COMMENT);
    readThis(Tokens.ON);
    switch(token.tokenType) {
        case Tokens.ROUTINE:
        case Tokens.TABLE:
            {
                type = token.tokenType == Tokens.ROUTINE ? SchemaObject.ROUTINE : SchemaObject.TABLE;
                read();
                checkIsSchemaObjectName();
                name = database.nameManager.newHsqlName(token.tokenString, token.isDelimitedIdentifier, type);
                if (token.namePrefix == null) {
                    name.schema = session.getCurrentSchemaHsqlName();
                } else {
                    name.schema = database.nameManager.newHsqlName(token.namePrefix, token.isDelimitedPrefix, SchemaObject.SCHEMA);
                }
                read();
                break;
            }
        case Tokens.COLUMN:
            {
                read();
                checkIsSchemaObjectName();
                name = database.nameManager.newHsqlName(token.tokenString, token.isDelimitedIdentifier, SchemaObject.COLUMN);
                if (token.namePrefix == null) {
                    throw Error.error(ErrorCode.X_42501);
                }
                name.parent = database.nameManager.newHsqlName(token.namePrefix, token.isDelimitedPrefix, SchemaObject.TABLE);
                if (token.namePrePrefix == null) {
                    name.parent.schema = session.getCurrentSchemaHsqlName();
                } else {
                    name.parent.schema = database.nameManager.newHsqlName(token.namePrePrefix, token.isDelimitedPrePrefix, SchemaObject.TABLE);
                }
                read();
                break;
            }
        default:
            throw unexpectedToken();
    }
    readThis(Tokens.IS);
    String comment = readQuotedString();
    Object[] arguments = new Object[] { name, comment };
    return new StatementSchema(null, StatementTypes.COMMENT, arguments);
}
