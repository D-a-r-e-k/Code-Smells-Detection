static Object[] getUpdatedData(Session session, Expression[] targets, Table targetTable, int[] columnMap, Expression[] colExpressions, Type[] colTypes, Object[] oldData) {
    Object[] data = targetTable.getEmptyRowData();
    System.arraycopy(oldData, 0, data, 0, data.length);
    for (int i = 0, ix = 0; i < columnMap.length; ) {
        Expression expr = colExpressions[ix++];
        if (expr.getType() == OpTypes.ROW) {
            Object[] values = expr.getRowValue(session);
            for (int j = 0; j < values.length; j++, i++) {
                int colIndex = columnMap[i];
                Expression e = expr.nodes[j];
                // transitional - still supporting null for identity generation 
                if (targetTable.identityColumn == colIndex) {
                    if (e.getType() == OpTypes.VALUE && e.valueData == null) {
                        continue;
                    }
                }
                if (e.getType() == OpTypes.DEFAULT) {
                    if (targetTable.identityColumn == colIndex) {
                        continue;
                    }
                    data[colIndex] = targetTable.colDefaults[colIndex].getValue(session);
                    continue;
                }
                data[colIndex] = colTypes[colIndex].convertToType(session, values[j], e.dataType);
            }
        } else if (expr.getType() == OpTypes.ROW_SUBQUERY) {
            Object[] values = expr.getRowValue(session);
            for (int j = 0; j < values.length; j++, i++) {
                int colIndex = columnMap[i];
                Type colType = expr.subQuery.queryExpression.getMetaData().columnTypes[j];
                data[colIndex] = colTypes[colIndex].convertToType(session, values[j], colType);
            }
        } else {
            int colIndex = columnMap[i];
            if (expr.getType() == OpTypes.DEFAULT) {
                if (targetTable.identityColumn == colIndex) {
                    i++;
                    continue;
                }
                data[colIndex] = targetTable.colDefaults[colIndex].getValue(session);
                i++;
                continue;
            }
            Object value = expr.getValue(session);
            if (targets[i].getType() == OpTypes.ARRAY_ACCESS) {
                data[colIndex] = ((ExpressionAccessor) targets[i]).getUpdatedArray(session, (Object[]) data[colIndex], value, true);
            } else {
                data[colIndex] = colTypes[colIndex].convertToType(session, value, expr.dataType);
            }
            i++;
        }
    }
    return data;
}
