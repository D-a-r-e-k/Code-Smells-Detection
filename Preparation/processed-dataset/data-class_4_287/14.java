private Expression resolveColumnReferencesInGroupBy(Expression expression) {
    if (expression == null) {
        return null;
    }
    HsqlList list = expression.resolveColumnReferences(rangeVariables, rangeVariables.length, null, false);
    if (list != null) {
        // if not resolved, resolve as simple alias 
        if (expression.getType() == OpTypes.COLUMN) {
            Expression resolved = expression.replaceAliasInOrderBy(exprColumns, indexLimitVisible);
            if (resolved != expression) {
                return resolved;
            }
        }
        // resolve and allocate to throw exception 
        resolveColumnReferencesAndAllocate(expression, rangeVariables.length, false);
    }
    return expression;
}
