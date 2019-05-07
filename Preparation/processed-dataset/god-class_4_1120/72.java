/**
     * checkValidCheckConstraint
     */
public void checkValidCheckConstraint() {
    OrderedHashSet set = null;
    set = collectAllExpressions(set, subqueryAggregateExpressionSet, emptyExpressionSet);
    if (set != null && !set.isEmpty()) {
        throw Error.error(ErrorCode.X_0A000, "subquery in check constraint");
    }
}
