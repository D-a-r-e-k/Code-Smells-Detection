Expression replaceColumnReferences(RangeVariable range, Expression[] list) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] == null) {
            continue;
        }
        nodes[i] = nodes[i].replaceColumnReferences(range, list);
    }
    if (subQuery != null && subQuery.queryExpression != null) {
        subQuery.queryExpression.replaceColumnReference(range, list);
    }
    return this;
}
