void processAlterColumn(Table table, ColumnSchema column, int columnIndex) {
    int position = getPosition();
    switch(token.tokenType) {
        case Tokens.RENAME:
            {
                read();
                readThis(Tokens.TO);
                processAlterColumnRename(table, column);
                return;
            }
        case Tokens.DROP:
            {
                read();
                if (token.tokenType == Tokens.DEFAULT) {
                    read();
                    TableWorks tw = new TableWorks(session, table);
                    tw.setColDefaultExpression(columnIndex, null);
                    return;
                } else if (token.tokenType == Tokens.GENERATED) {
                    read();
                    column.setIdentity(null);
                    table.setColumnTypeVars(columnIndex);
                    return;
                } else {
                    throw unexpectedToken();
                }
            }
        case Tokens.SET:
            {
                read();
                switch(token.tokenType) {
                    case Tokens.DATA:
                        {
                            read();
                            readThis(Tokens.TYPE);
                            processAlterColumnDataType(table, column);
                            return;
                        }
                    case Tokens.DEFAULT:
                        {
                            read();
                            //ALTER TABLE .. ALTER COLUMN .. SET DEFAULT 
                            TableWorks tw = new TableWorks(session, table);
                            Type type = column.getDataType();
                            Expression expr = this.readDefaultClause(type);
                            tw.setColDefaultExpression(columnIndex, expr);
                            return;
                        }
                    case Tokens.NOT:
                        {
                            //ALTER TABLE .. ALTER COLUMN .. SET NOT NULL 
                            read();
                            readThis(Tokens.NULL);
                            session.commit(false);
                            TableWorks tw = new TableWorks(session, table);
                            tw.setColNullability(column, false);
                            return;
                        }
                    case Tokens.NULL:
                        {
                            read();
                            //ALTER TABLE .. ALTER COLUMN .. SET NULL 
                            session.commit(false);
                            TableWorks tw = new TableWorks(session, table);
                            tw.setColNullability(column, true);
                            return;
                        }
                    default:
                        rewind(position);
                        read();
                        break;
                }
            }
        default:
    }
    if (token.tokenType == Tokens.SET || token.tokenType == Tokens.RESTART) {
        if (!column.isIdentity()) {
            throw Error.error(ErrorCode.X_42535);
        }
        processAlterColumnSequenceOptions(column);
        return;
    } else {
        processAlterColumnType(table, column, true);
        return;
    }
}
