/**
     * collects all range variables in expression tree
     */
void collectRangeVariables(RangeVariable[] rangeVariables, Set set) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
            nodes[i].collectRangeVariables(rangeVariables, set);
        }
    }
    if (subQuery != null && subQuery.queryExpression != null) {
        HsqlList unresolvedExpressions = subQuery.queryExpression.getUnresolvedExpressions();
        if (unresolvedExpressions != null) {
            for (int i = 0; i < unresolvedExpressions.size(); i++) {
                Expression e = (Expression) unresolvedExpressions.get(i);
                e.collectRangeVariables(rangeVariables, set);
            }
        }
    }
}
