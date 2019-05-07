/**
     * For MATCH SIMPLE and FULL expressions, nulls in left are handled
     * prior to calling this method
     */
private Boolean compareValues(Session session, Object[] left, Object[] right) {
    int result = 0;
    boolean hasNull = false;
    if (left == null || right == null) {
        return null;
    }
    Object[] leftList = (Object[]) left;
    Object[] rightList = (Object[]) right;
    for (int i = 0; i < nodes[LEFT].nodes.length; i++) {
        if (leftList[i] == null) {
            if (opType == OpTypes.MATCH_PARTIAL || opType == OpTypes.MATCH_UNIQUE_PARTIAL) {
                continue;
            }
            hasNull = true;
        }
        if (rightList[i] == null) {
            hasNull = true;
        }
        Object leftValue = leftList[i];
        Object rightValue = rightList[i];
        Type[] types = nodes[LEFT].nodeDataTypes;
        result = types[i].compare(session, leftValue, rightValue);
        if (result != 0) {
            break;
        }
    }
    switch(opType) {
        case OpTypes.MATCH_SIMPLE:
        case OpTypes.MATCH_UNIQUE_SIMPLE:
        case OpTypes.MATCH_PARTIAL:
        case OpTypes.MATCH_UNIQUE_PARTIAL:
        case OpTypes.MATCH_FULL:
        case OpTypes.MATCH_UNIQUE_FULL:
        case OpTypes.NOT_DISTINCT:
            return result == 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.IN:
        case OpTypes.EQUAL:
            if (hasNull) {
                return null;
            }
            return result == 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.NOT_EQUAL:
            if (hasNull) {
                return null;
            }
            return result != 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.GREATER:
            if (hasNull) {
                return null;
            }
            return result > 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.GREATER_EQUAL:
            if (hasNull) {
                return null;
            }
            return result >= 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.SMALLER_EQUAL:
            if (hasNull) {
                return null;
            }
            return result <= 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.SMALLER:
            if (hasNull) {
                return null;
            }
            return result < 0 ? Boolean.TRUE : Boolean.FALSE;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionLogical");
    }
}
