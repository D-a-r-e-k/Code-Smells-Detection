void resolveOuterReferencesAndTypes(RangeVariable[] rangeVars, Expression e) {
    HsqlList unresolved = e.resolveColumnReferences(rangeVars, rangeVars.length, null, false);
    unresolved = Expression.resolveColumnSet(rangeVars, rangeVars.length, unresolved, null);
    ExpressionColumn.checkColumnsResolved(unresolved);
    e.resolveTypes(session, null);
}
