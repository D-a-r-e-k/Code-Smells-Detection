/**
     * For MATCH SIMPLE and FULL expressions, nulls in left are handled
     * prior to calling this method
     */
private Boolean compareValues(Session session, Object left, Object right) {
    int result = 0;
    if (left == null || right == null) {
        return null;
    }
    result = nodes[LEFT].dataType.compare(session, left, right);
    switch(opType) {
        case OpTypes.EQUAL:
            return result == 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.NOT_EQUAL:
            return result != 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.GREATER:
            return result > 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.GREATER_EQUAL:
            return result >= 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.SMALLER_EQUAL:
            return result <= 0 ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.SMALLER:
            return result < 0 ? Boolean.TRUE : Boolean.FALSE;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionLogical");
    }
}
