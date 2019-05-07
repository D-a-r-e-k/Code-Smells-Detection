void collectRangeVariables(RangeVariable[] rangeVars, Set set) {
    for (int i = 0; i < indexStartAggregates; i++) {
        exprColumns[i].collectRangeVariables(rangeVars, set);
    }
    if (queryCondition != null) {
        queryCondition.collectRangeVariables(rangeVars, set);
    }
    if (havingCondition != null) {
        havingCondition.collectRangeVariables(rangeVars, set);
    }
}
