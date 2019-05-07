public void replaceColumnReference(RangeVariable range, Expression[] list) {
    for (int i = 0; i < indexStartAggregates; i++) {
        exprColumns[i].replaceColumnReferences(range, list);
    }
    if (queryCondition != null) {
        queryCondition.replaceColumnReferences(range, list);
    }
    if (havingCondition != null) {
        havingCondition.replaceColumnReferences(range, list);
    }
    for (int i = 0, len = rangeVariables.length; i < len; i++) {
    }
}
