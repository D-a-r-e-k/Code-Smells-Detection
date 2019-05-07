/**
     * collects all schema objects
     */
void collectObjectNames(Set set) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
            nodes[i].collectObjectNames(set);
        }
    }
    if (subQuery != null) {
        if (subQuery.queryExpression != null) {
            subQuery.queryExpression.collectObjectNames(set);
        }
    }
}
