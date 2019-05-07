/**
     * For GROUP only.
     */
boolean isComposedOf(Expression exprList[], int start, int end, OrderedIntHashSet excludeSet) {
    if (opType == OpTypes.VALUE) {
        return true;
    }
    if (excludeSet.contains(opType)) {
        return true;
    }
    for (int i = start; i < end; i++) {
        if (equals(exprList[i])) {
            return true;
        }
    }
    switch(opType) {
        case OpTypes.LIKE:
        case OpTypes.MATCH_SIMPLE:
        case OpTypes.MATCH_PARTIAL:
        case OpTypes.MATCH_FULL:
        case OpTypes.MATCH_UNIQUE_SIMPLE:
        case OpTypes.MATCH_UNIQUE_PARTIAL:
        case OpTypes.MATCH_UNIQUE_FULL:
        case OpTypes.UNIQUE:
        case OpTypes.EXISTS:
        case OpTypes.ARRAY:
        case OpTypes.ARRAY_SUBQUERY:
        case OpTypes.TABLE_SUBQUERY:
        case OpTypes.ROW_SUBQUERY:
        // 
        case OpTypes.COUNT:
        case OpTypes.SUM:
        case OpTypes.MIN:
        case OpTypes.MAX:
        case OpTypes.AVG:
        case OpTypes.EVERY:
        case OpTypes.SOME:
        case OpTypes.STDDEV_POP:
        case OpTypes.STDDEV_SAMP:
        case OpTypes.VAR_POP:
        case OpTypes.VAR_SAMP:
            return false;
    }
    if (nodes.length == 0) {
        return false;
    }
    boolean result = true;
    for (int i = 0; i < nodes.length; i++) {
        result &= (nodes[i] == null || nodes[i].isComposedOf(exprList, start, end, excludeSet));
    }
    return result;
}
