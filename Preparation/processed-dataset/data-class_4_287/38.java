private void createResultMetaData() {
    columnTypes = new Type[indexLimitData];
    for (int i = 0; i < indexStartAggregates; i++) {
        Expression e = exprColumns[i];
        columnTypes[i] = e.getDataType();
    }
    for (int i = indexLimitVisible; i < indexLimitRowId; i++) {
        if (i == indexLimitVisible) {
            columnTypes[i] = Type.SQL_BIGINT;
        } else {
            columnTypes[i] = Type.SQL_ALL_TYPES;
        }
    }
    for (int i = indexLimitRowId; i < indexLimitData; i++) {
        Expression e = exprColumns[i];
        columnTypes[i] = e.getDataType();
    }
    resultMetaData = ResultMetaData.newResultMetaData(columnTypes, columnMap, indexLimitVisible, indexLimitRowId);
    for (int i = 0; i < indexLimitVisible; i++) {
        Expression e = exprColumns[i];
        resultMetaData.columnTypes[i] = e.getDataType();
        if (i < indexLimitVisible) {
            ColumnBase column = e.getColumn();
            if (column != null) {
                resultMetaData.columns[i] = column;
                resultMetaData.columnLabels[i] = e.getAlias();
                continue;
            }
            column = new ColumnBase();
            column.setType(e.getDataType());
            resultMetaData.columns[i] = column;
            resultMetaData.columnLabels[i] = e.getAlias();
        }
    }
}
