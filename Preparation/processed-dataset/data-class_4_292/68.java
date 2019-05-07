/**
     * collect all expressions of a set of expression types appearing anywhere
     * in a select statement and its subselects, etc.
     */
OrderedHashSet collectAllExpressions(OrderedHashSet set, OrderedIntHashSet typeSet, OrderedIntHashSet stopAtTypeSet) {
    if (stopAtTypeSet.contains(opType)) {
        return set;
    }
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
            set = nodes[i].collectAllExpressions(set, typeSet, stopAtTypeSet);
        }
    }
    if (typeSet.contains(opType)) {
        if (set == null) {
            set = new OrderedHashSet();
        }
        set.add(this);
    }
    if (subQuery != null && subQuery.queryExpression != null) {
        set = subQuery.queryExpression.collectAllExpressions(set, typeSet, stopAtTypeSet);
    }
    return set;
}
