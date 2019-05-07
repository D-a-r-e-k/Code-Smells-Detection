void resolveColumnRefernecesInOrderBy(SortAndSlice sortAndSlice) {
    // replace the aliases with expressions 
    // replace column names with expressions and resolve the table columns 
    int orderCount = sortAndSlice.getOrderLength();
    for (int i = 0; i < orderCount; i++) {
        ExpressionOrderBy e = (ExpressionOrderBy) sortAndSlice.exprList.get(i);
        replaceColumnIndexInOrderBy(e);
        if (e.getLeftNode().queryTableColumnIndex != -1) {
            continue;
        }
        if (sortAndSlice.sortUnion) {
            if (e.getLeftNode().getType() != OpTypes.COLUMN) {
                throw Error.error(ErrorCode.X_42576);
            }
        }
        e.replaceAliasInOrderBy(exprColumns, indexLimitVisible);
        resolveColumnReferencesAndAllocate(e, rangeVariables.length, false);
        if (isAggregated || isGrouped) {
            boolean check = e.getLeftNode().isComposedOf(exprColumns, 0, indexLimitVisible + groupByColumnCount, Expression.aggregateFunctionSet);
            if (!check) {
                throw Error.error(ErrorCode.X_42576);
            }
        }
    }
    sortAndSlice.prepare(this);
}
