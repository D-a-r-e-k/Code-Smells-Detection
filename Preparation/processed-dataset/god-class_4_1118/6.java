private void processAlterTable() {
    String tableName = token.tokenString;
    HsqlName schema = session.getSchemaHsqlName(token.namePrefix);
    checkSchemaUpdateAuthorisation(schema);
    Table t = database.schemaManager.getUserTable(session, tableName, schema.name);
    if (t.isView()) {
        throw Error.error(ErrorCode.X_42501, tableName);
    }
    read();
    switch(token.tokenType) {
        case Tokens.RENAME:
            {
                read();
                readThis(Tokens.TO);
                processAlterTableRename(t);
                return;
            }
        case Tokens.ADD:
            {
                read();
                HsqlName cname = null;
                if (token.tokenType == Tokens.CONSTRAINT) {
                    read();
                    cname = readNewDependentSchemaObjectName(t.getName(), SchemaObject.CONSTRAINT);
                    database.schemaManager.checkSchemaObjectNotExists(cname);
                }
                switch(token.tokenType) {
                    case Tokens.FOREIGN:
                        read();
                        readThis(Tokens.KEY);
                        processAlterTableAddForeignKeyConstraint(t, cname);
                        return;
                    case Tokens.UNIQUE:
                        read();
                        processAlterTableAddUniqueConstraint(t, cname);
                        return;
                    case Tokens.CHECK:
                        read();
                        processAlterTableAddCheckConstraint(t, cname);
                        return;
                    case Tokens.PRIMARY:
                        read();
                        readThis(Tokens.KEY);
                        processAlterTableAddPrimaryKey(t, cname);
                        return;
                    case Tokens.COLUMN:
                        if (cname != null) {
                            throw unexpectedToken();
                        }
                        read();
                        checkIsSimpleName();
                        processAlterTableAddColumn(t);
                        return;
                    default:
                        if (cname != null) {
                            throw unexpectedToken();
                        }
                        checkIsSimpleName();
                        processAlterTableAddColumn(t);
                        return;
                }
            }
        case Tokens.DROP:
            {
                read();
                switch(token.tokenType) {
                    case Tokens.PRIMARY:
                        {
                            boolean cascade = false;
                            read();
                            readThis(Tokens.KEY);
                            if (token.tokenType == Tokens.CASCADE) {
                                read();
                                cascade = true;
                            }
                            if (t.hasPrimaryKey()) {
                                processAlterTableDropConstraint(t, t.getPrimaryConstraint().getName().name, cascade);
                            } else {
                                throw Error.error(ErrorCode.X_42501);
                            }
                            return;
                        }
                    case Tokens.CONSTRAINT:
                        {
                            boolean cascade = false;
                            read();
                            SchemaObject object = readSchemaObjectName(t.getName(), SchemaObject.CONSTRAINT);
                            if (token.tokenType == Tokens.RESTRICT) {
                                read();
                            } else if (token.tokenType == Tokens.CASCADE) {
                                read();
                                cascade = true;
                            }
                            processAlterTableDropConstraint(t, object.getName().name, cascade);
                            //                        read(); 
                            return;
                        }
                    case Tokens.COLUMN:
                        read();
                    // fall through 
                    default:
                        {
                            checkIsSimpleName();
                            String name = token.tokenString;
                            boolean cascade = false;
                            read();
                            if (token.tokenType == Tokens.RESTRICT) {
                                read();
                            } else if (token.tokenType == Tokens.CASCADE) {
                                read();
                                cascade = true;
                            }
                            processAlterTableDropColumn(t, name, cascade);
                            return;
                        }
                }
            }
        case Tokens.ALTER:
            {
                read();
                if (token.tokenType == Tokens.COLUMN) {
                    read();
                }
                int columnIndex = t.getColumnIndex(token.tokenString);
                ColumnSchema column = t.getColumn(columnIndex);
                read();
                processAlterColumn(t, column, columnIndex);
                return;
            }
        default:
            {
                throw unexpectedToken();
            }
    }
}
