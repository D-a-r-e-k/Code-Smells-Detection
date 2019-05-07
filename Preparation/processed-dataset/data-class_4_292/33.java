/**
     * return true if given RangeVariable is used in expression tree
     */
boolean hasReference(RangeVariable range) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
            if (nodes[i].hasReference(range)) {
                return true;
            }
        }
    }
    if (subQuery != null && subQuery.queryExpression != null) {
        if (subQuery.queryExpression.hasReference(range)) {
            return true;
        }
    }
    return false;
}
