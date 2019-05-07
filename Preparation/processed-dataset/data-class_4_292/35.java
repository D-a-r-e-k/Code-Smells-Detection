public HsqlList resolveColumnReferences(RangeVariable[] rangeVarArray, int rangeCount, HsqlList unresolvedSet, boolean acceptsSequences) {
    if (opType == OpTypes.VALUE) {
        return unresolvedSet;
    }
    switch(opType) {
        case OpTypes.CASEWHEN:
            acceptsSequences = false;
            break;
        case OpTypes.TABLE:
            {
                HsqlList localSet = null;
                for (int i = 0; i < nodes.length; i++) {
                    if (nodes[i] == null) {
                        continue;
                    }
                    localSet = nodes[i].resolveColumnReferences(RangeVariable.emptyArray, localSet);
                }
                if (localSet != null) {
                    isCorrelated = true;
                    if (subQuery != null) {
                        subQuery.setCorrelated();
                    }
                    for (int i = 0; i < localSet.size(); i++) {
                        Expression e = (Expression) localSet.get(i);
                        unresolvedSet = e.resolveColumnReferences(rangeVarArray, unresolvedSet);
                    }
                    unresolvedSet = Expression.resolveColumnSet(rangeVarArray, rangeVarArray.length, localSet, unresolvedSet);
                }
                return unresolvedSet;
            }
    }
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] == null) {
            continue;
        }
        unresolvedSet = nodes[i].resolveColumnReferences(rangeVarArray, rangeCount, unresolvedSet, acceptsSequences);
    }
    switch(opType) {
        case OpTypes.ARRAY:
            break;
        case OpTypes.ARRAY_SUBQUERY:
        case OpTypes.ROW_SUBQUERY:
        case OpTypes.TABLE_SUBQUERY:
            {
                QueryExpression queryExpression = subQuery.queryExpression;
                if (!queryExpression.areColumnsResolved()) {
                    isCorrelated = true;
                    subQuery.setCorrelated();
                    // take to enclosing context 
                    if (unresolvedSet == null) {
                        unresolvedSet = new ArrayListIdentity();
                    }
                    unresolvedSet.addAll(queryExpression.getUnresolvedExpressions());
                }
                break;
            }
        default:
    }
    return unresolvedSet;
}
