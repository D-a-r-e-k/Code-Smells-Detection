OrderedHashSet collectAllSubqueries(OrderedHashSet set) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
            set = nodes[i].collectAllSubqueries(set);
        }
    }
    if (subQuery != null) {
        if (set == null) {
            set = new OrderedHashSet();
        }
        set.add(subQuery);
        if (subQuery.queryExpression != null) {
            OrderedHashSet tempSet = subQuery.queryExpression.getSubqueries();
            set = OrderedHashSet.addAll(set, tempSet);
        }
    }
    return set;
}
