private ColumnSchema[] readLikeTable(Table table) {
    read();
    boolean generated = false;
    boolean identity = false;
    boolean defaults = false;
    Table likeTable = readTableName();
    OrderedIntHashSet set = new OrderedIntHashSet();
    while (true) {
        boolean including = token.tokenType == Tokens.INCLUDING;
        if (!including && token.tokenType != Tokens.EXCLUDING) {
            break;
        }
        read();
        switch(token.tokenType) {
            case Tokens.GENERATED:
                if (!set.add(token.tokenType)) {
                    throw unexpectedToken();
                }
                generated = including;
                break;
            case Tokens.IDENTITY:
                if (!set.add(token.tokenType)) {
                    throw unexpectedToken();
                }
                identity = including;
                break;
            case Tokens.DEFAULTS:
                if (!set.add(token.tokenType)) {
                    throw unexpectedToken();
                }
                defaults = including;
                break;
            default:
                throw unexpectedToken();
        }
        read();
    }
    ColumnSchema[] columnList = new ColumnSchema[likeTable.getColumnCount()];
    for (int i = 0; i < columnList.length; i++) {
        ColumnSchema column = likeTable.getColumn(i).duplicate();
        HsqlName name = database.nameManager.newColumnSchemaHsqlName(table.getName(), column.getName());
        column.setName(name);
        column.setNullable(true);
        column.setPrimaryKey(false);
        if (identity) {
            if (column.isIdentity()) {
                column.setIdentity(column.getIdentitySequence().duplicate());
            }
        } else {
            column.setIdentity(null);
        }
        if (!defaults) {
            column.setDefaultExpression(null);
        }
        if (!generated) {
            column.setGeneratingExpression(null);
        }
        columnList[i] = column;
    }
    return columnList;
}
