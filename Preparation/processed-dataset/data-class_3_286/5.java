void resolveUpdateExpressions(Table targetTable, RangeVariable[] rangeVariables, int[] columnMap, Expression[] colExpressions, RangeVariable[] outerRanges) {
    HsqlList unresolved = null;
    int enforcedDefaultIndex = -1;
    if (targetTable.hasIdentityColumn() && targetTable.identitySequence.isAlways()) {
        enforcedDefaultIndex = targetTable.getIdentityColumnIndex();
    }
    for (int i = 0, ix = 0; i < columnMap.length; ix++) {
        Expression expr = colExpressions[ix];
        Expression e;
        // no generated column can be updated 
        if (targetTable.colGenerated[columnMap[i]]) {
            throw Error.error(ErrorCode.X_42513);
        }
        if (expr.getType() == OpTypes.ROW) {
            Expression[] elements = expr.nodes;
            for (int j = 0; j < elements.length; j++, i++) {
                e = elements[j];
                if (enforcedDefaultIndex == columnMap[i]) {
                    if (e.getType() != OpTypes.DEFAULT) {
                        throw Error.error(ErrorCode.X_42541);
                    }
                }
                if (e.isUnresolvedParam()) {
                    e.setAttributesAsColumn(targetTable.getColumn(columnMap[i]), true);
                } else if (e.getType() == OpTypes.DEFAULT) {
                    if (targetTable.colDefaults[columnMap[i]] == null && targetTable.identityColumn != columnMap[i]) {
                        throw Error.error(ErrorCode.X_42544);
                    }
                } else {
                    unresolved = expr.resolveColumnReferences(outerRanges, null);
                    unresolved = Expression.resolveColumnSet(rangeVariables, rangeVariables.length, unresolved, null);
                    ExpressionColumn.checkColumnsResolved(unresolved);
                    unresolved = null;
                    e.resolveTypes(session, null);
                }
            }
        } else if (expr.getType() == OpTypes.ROW_SUBQUERY) {
            unresolved = expr.resolveColumnReferences(outerRanges, null);
            unresolved = Expression.resolveColumnSet(rangeVariables, rangeVariables.length, unresolved, null);
            ExpressionColumn.checkColumnsResolved(unresolved);
            expr.resolveTypes(session, null);
            int count = expr.subQuery.queryExpression.getColumnCount();
            for (int j = 0; j < count; j++, i++) {
                if (enforcedDefaultIndex == columnMap[i]) {
                    throw Error.error(ErrorCode.X_42541);
                }
            }
        } else {
            e = expr;
            if (enforcedDefaultIndex == columnMap[i]) {
                if (e.getType() != OpTypes.DEFAULT) {
                    throw Error.error(ErrorCode.X_42541);
                }
            }
            if (e.isUnresolvedParam()) {
                e.setAttributesAsColumn(targetTable.getColumn(columnMap[i]), true);
            } else if (e.getType() == OpTypes.DEFAULT) {
                if (targetTable.colDefaults[columnMap[i]] == null && targetTable.identityColumn != columnMap[i]) {
                    throw Error.error(ErrorCode.X_42544);
                }
            } else {
                unresolved = expr.resolveColumnReferences(outerRanges, null);
                unresolved = Expression.resolveColumnSet(rangeVariables, rangeVariables.length, unresolved, null);
                ExpressionColumn.checkColumnsResolved(unresolved);
                e.resolveTypes(session, null);
            }
            i++;
        }
    }
}
