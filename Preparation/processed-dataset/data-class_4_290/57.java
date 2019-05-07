Statement compileAlterColumn(Table table, ColumnSchema column, int columnIndex) {
    int position = getPosition();
    switch(token.tokenType) {
        case Tokens.RENAME:
            {
                read();
                readThis(Tokens.TO);
                return compileAlterColumnRename(table, column);
            }
        case Tokens.DROP:
            {
                read();
                if (token.tokenType == Tokens.DEFAULT) {
                    read();
                    return compileAlterColumnDropDefault(table, column, columnIndex);
                } else if (token.tokenType == Tokens.GENERATED) {
                    read();
                    return compileAlterColumnDropGenerated(table, column, columnIndex);
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
                            return compileAlterColumnDataType(table, column);
                        }
                    case Tokens.DEFAULT:
                        {
                            read();
                            //ALTER TABLE .. ALTER COLUMN .. SET DEFAULT 
                            Type type = column.getDataType();
                            Expression expr = this.readDefaultClause(type);
                            return compileAlterColumnSetDefault(table, column, expr);
                        }
                    case Tokens.NOT:
                        {
                            //ALTER TABLE .. ALTER COLUMN .. SET NOT NULL 
                            read();
                            readThis(Tokens.NULL);
                            return compileAlterColumnSetNullability(table, column, false);
                        }
                    case Tokens.NULL:
                        {
                            read();
                            return compileAlterColumnSetNullability(table, column, true);
                        }
                    default:
                        rewind(position);
                        read();
                        break;
                }
            }
        // fall through 
        default:
    }
    if (token.tokenType == Tokens.SET || token.tokenType == Tokens.RESTART) {
        if (!column.isIdentity()) {
            throw Error.error(ErrorCode.X_42535);
        }
        return compileAlterColumnSequenceOptions(table, column);
    } else {
        return compileAlterColumnType(table, column);
    }
}
