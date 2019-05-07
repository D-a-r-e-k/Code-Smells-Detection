StatementSchema compileCreateTableBody(Table table) {
    HsqlArrayList tempConstraints = new HsqlArrayList();
    if (token.tokenType == Tokens.AS) {
        return readTableAsSubqueryDefinition(table);
    }
    int position = getPosition();
    readThis(Tokens.OPENBRACKET);
    {
        Constraint c = new Constraint(null, null, SchemaObject.ConstraintTypes.TEMP);
        tempConstraints.add(c);
    }
    boolean start = true;
    boolean startPart = true;
    boolean end = false;
    while (!end) {
        switch(token.tokenType) {
            case Tokens.LIKE:
                {
                    ColumnSchema[] likeColumns = readLikeTable(table);
                    for (int i = 0; i < likeColumns.length; i++) {
                        table.addColumn(likeColumns[i]);
                    }
                    start = false;
                    startPart = false;
                    break;
                }
            case Tokens.CONSTRAINT:
            case Tokens.PRIMARY:
            case Tokens.FOREIGN:
            case Tokens.UNIQUE:
            case Tokens.CHECK:
                if (!startPart) {
                    throw unexpectedToken();
                }
                readConstraint(table, tempConstraints);
                start = false;
                startPart = false;
                break;
            case Tokens.COMMA:
                if (startPart) {
                    throw unexpectedToken();
                }
                read();
                startPart = true;
                break;
            case Tokens.CLOSEBRACKET:
                read();
                end = true;
                break;
            default:
                if (!startPart) {
                    throw unexpectedToken();
                }
                checkIsSchemaObjectName();
                HsqlName hsqlName = database.nameManager.newColumnHsqlName(table.getName(), token.tokenString, isDelimitedIdentifier());
                read();
                ColumnSchema newcolumn = readColumnDefinitionOrNull(table, hsqlName, tempConstraints);
                if (newcolumn == null) {
                    if (start) {
                        rewind(position);
                        return readTableAsSubqueryDefinition(table);
                    } else {
                        throw Error.error(ErrorCode.X_42000);
                    }
                }
                table.addColumn(newcolumn);
                start = false;
                startPart = false;
        }
    }
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
    Object[] args = new Object[] { table, tempConstraints, null };
    String sql = getLastPart();
    return new StatementSchema(sql, StatementTypes.CREATE_TABLE, args);
}
