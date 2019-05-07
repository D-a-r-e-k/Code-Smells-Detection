void replaceRangeVariables(RangeVariable[] ranges, RangeVariable[] newRanges) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] == null) {
            continue;
        }
        nodes[i].replaceRangeVariables(ranges, newRanges);
    }
    if (subQuery != null && subQuery.queryExpression != null) {
        subQuery.queryExpression.replaceRangeVariables(ranges, newRanges);
    }
}
