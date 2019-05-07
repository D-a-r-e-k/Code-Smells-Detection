/**
     * For HAVING only.
     */
boolean isComposedOf(OrderedHashSet expressions, OrderedIntHashSet excludeSet) {
    if (opType == OpTypes.VALUE || opType == OpTypes.DYNAMIC_PARAM || opType == OpTypes.PARAMETER || opType == OpTypes.VARIABLE) {
        return true;
    }
    if (excludeSet.contains(opType)) {
        return true;
    }
    for (int i = 0; i < expressions.size(); i++) {
        if (equals(expressions.get(i))) {
            return true;
        }
    }
    switch(opType) {
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
    /*
        case OpCodes.LIKE :
        case OpCodes.ALL :
        case OpCodes.ANY :
        case OpCodes.IN :
        case OpCodes.MATCH_SIMPLE :
        case OpCodes.MATCH_PARTIAL :
        case OpCodes.MATCH_FULL :
        case OpCodes.MATCH_UNIQUE_SIMPLE :
        case OpCodes.MATCH_UNIQUE_PARTIAL :
        case OpCodes.MATCH_UNIQUE_FULL :
        case OpCodes.UNIQUE :
        case OpCodes.EXISTS :
        case OpCodes.TABLE_SUBQUERY :
        case OpCodes.ROW_SUBQUERY :
*/
    if (nodes.length == 0) {
        return false;
    }
    boolean result = true;
    for (int i = 0; i < nodes.length; i++) {
        result &= (nodes[i] == null || nodes[i].isComposedOf(expressions, excludeSet));
    }
    return result;
}
