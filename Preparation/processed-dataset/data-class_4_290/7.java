Statement compileAlterTable() {
    read();
    String tableName = token.tokenString;
    HsqlName schema = session.getSchemaHsqlName(token.namePrefix);
    Table t = database.schemaManager.getUserTable(session, tableName, schema.name);
    read();
    switch(token.tokenType) {
        case Tokens.RENAME:
            {
                read();
                readThis(Tokens.TO);
                return compileRenameObject(t.getName(), SchemaObject.TABLE);
            }
        case Tokens.ADD:
            {
                read();
                HsqlName cname = null;
                if (token.tokenType == Tokens.CONSTRAINT) {
                    read();
                    cname = readNewDependentSchemaObjectName(t.getName(), SchemaObject.CONSTRAINT);
                }
                switch(token.tokenType) {
                    case Tokens.FOREIGN:
                        read();
                        readThis(Tokens.KEY);
                        return compileAlterTableAddForeignKeyConstraint(t, cname);
                    case Tokens.UNIQUE:
                        read();
                        return compileAlterTableAddUniqueConstraint(t, cname);
                    case Tokens.CHECK:
                        read();
                        return compileAlterTableAddCheckConstraint(t, cname);
                    case Tokens.PRIMARY:
                        read();
                        readThis(Tokens.KEY);
                        return compileAlterTableAddPrimaryKey(t, cname);
                    case Tokens.COLUMN:
                        if (cname != null) {
                            throw unexpectedToken();
                        }
                        read();
                        checkIsSimpleName();
                        return compileAlterTableAddColumn(t);
                    default:
                        if (cname != null) {
                            throw unexpectedToken();
                        }
                        checkIsSimpleName();
                        return compileAlterTableAddColumn(t);
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
                            return compileAlterTableDropPrimaryKey(t);
                        }
                    case Tokens.CONSTRAINT:
                        {
                            read();
                            return compileAlterTableDropConstraint(t);
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
                            return compileAlterTableDropColumn(t, name, cascade);
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
                return compileAlterColumn(t, column, columnIndex);
            }
        default:
            {
                throw unexpectedToken();
            }
    }
}
