/**
     * Resolves all column expressions in the GROUP BY clause and beyond.
     * Replaces any alias column expression in the ORDER BY cluase
     * with the actual select column expression.
     */
private void resolveColumnReferences() {
    if (isDistinctSelect || isGrouped) {
        acceptsSequences = false;
    }
    for (int i = 0; i < rangeVariables.length; i++) {
        Expression e = rangeVariables[i].getJoinCondition();
        if (e == null) {
            continue;
        }
        resolveColumnReferencesAndAllocate(e, i + 1, false);
    }
    resolveColumnReferencesAndAllocate(queryCondition, rangeVariables.length, false);
    if (resolvedSubqueryExpressions != null) {
        // subqueries in conditions not to be converted to SIMPLE_COLUMN 
        resolvedSubqueryExpressions.setSize(0);
    }
    for (int i = 0; i < indexLimitVisible; i++) {
        resolveColumnReferencesAndAllocate(exprColumns[i], rangeVariables.length, acceptsSequences);
    }
    for (int i = indexLimitVisible; i < indexStartHaving; i++) {
        exprColumns[i] = resolveColumnReferencesInGroupBy(exprColumns[i]);
    }
    for (int i = indexStartHaving; i < indexStartOrderBy; i++) {
        resolveColumnReferencesAndAllocate(exprColumns[i], rangeVariables.length, false);
    }
    resolveColumnRefernecesInOrderBy(sortAndSlice);
}
