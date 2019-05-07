Object[] getInsertData(Session session, Type[] colTypes, Expression[] rowArgs) {
    Object[] data = baseTable.getNewRowData(session);
    session.sessionData.startRowProcessing();
    for (int i = 0; i < rowArgs.length; i++) {
        Expression e = rowArgs[i];
        int colIndex = insertColumnMap[i];
        if (e.opType == OpTypes.DEFAULT) {
            if (baseTable.identityColumn == colIndex) {
                continue;
            }
            if (baseTable.colDefaults[colIndex] != null) {
                data[colIndex] = baseTable.colDefaults[colIndex].getValue(session);
                continue;
            }
            continue;
        }
        Object value = e.getValue(session);
        Type type = colTypes[colIndex];
        if (colTypes[colIndex] != e.dataType) {
            value = type.convertToType(session, value, e.dataType);
        }
        data[colIndex] = value;
    }
    return data;
}
