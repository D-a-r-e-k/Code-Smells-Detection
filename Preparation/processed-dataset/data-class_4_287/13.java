private void resolveColumnReferencesAndAllocate(Expression expression, int count, boolean withSequences) {
    if (expression == null) {
        return;
    }
    HsqlList list = expression.resolveColumnReferences(rangeVariables, count, null, withSequences);
    if (list != null) {
        for (int i = 0; i < list.size(); i++) {
            Expression e = (Expression) list.get(i);
            boolean resolved;
            if (e.isSelfAggregate()) {
                resolved = resolveColumnReferences(e.getLeftNode(), count, false);
            } else {
                resolved = resolveColumnReferences(e, count, withSequences);
            }
            if (resolved) {
                if (e.isSelfAggregate()) {
                    if (aggregateSet == null) {
                        aggregateSet = new ArrayListIdentity();
                    }
                    aggregateSet.add(e);
                    isAggregated = true;
                    expression.setAggregate();
                }
                if (resolvedSubqueryExpressions == null) {
                    resolvedSubqueryExpressions = new ArrayListIdentity();
                }
                resolvedSubqueryExpressions.add(e);
            } else {
                if (unresolvedExpressions == null) {
                    unresolvedExpressions = new ArrayListIdentity();
                }
                unresolvedExpressions.add(e);
            }
        }
    }
}
