public Object getValue(Session session) {
    switch(opType) {
        case OpTypes.VALUE:
            return valueData;
        case OpTypes.SIMPLE_COLUMN:
            {
                Object[] data = (Object[]) session.sessionContext.rangeIterators[rangePosition].getCurrent();
                return data[columnIndex];
            }
        case OpTypes.NEGATE:
            return ((NumberType) dataType).negate(nodes[LEFT].getValue(session, nodes[LEFT].dataType));
        case OpTypes.IS_NULL:
            return nodes[LEFT].getValue(session) == null ? Boolean.TRUE : Boolean.FALSE;
        case OpTypes.OVERLAPS:
            {
                Object[] left = nodes[LEFT].getRowValue(session);
                Object[] right = nodes[RIGHT].getRowValue(session);
                return DateTimeType.overlaps(session, left, nodes[LEFT].nodeDataTypes, right, nodes[RIGHT].nodeDataTypes);
            }
        case OpTypes.IN:
            {
                return testInCondition(session, nodes[LEFT].getRowValue(session));
            }
        case OpTypes.MATCH_SIMPLE:
        case OpTypes.MATCH_PARTIAL:
        case OpTypes.MATCH_FULL:
        case OpTypes.MATCH_UNIQUE_SIMPLE:
        case OpTypes.MATCH_UNIQUE_PARTIAL:
        case OpTypes.MATCH_UNIQUE_FULL:
            {
                return testMatchCondition(session, nodes[LEFT].getRowValue(session));
            }
        case OpTypes.UNIQUE:
            {
                nodes[LEFT].materialise(session);
                return nodes[LEFT].subQuery.hasUniqueNotNullRows(session) ? Boolean.TRUE : Boolean.FALSE;
            }
        case OpTypes.EXISTS:
            {
                return testExistsCondition(session);
            }
        case OpTypes.NOT:
            {
                Boolean result = (Boolean) nodes[LEFT].getValue(session);
                return result == null ? null : result.booleanValue() ? Boolean.FALSE : Boolean.TRUE;
            }
        case OpTypes.AND:
            {
                Boolean r1 = (Boolean) nodes[LEFT].getValue(session);
                if (Boolean.FALSE.equals(r1)) {
                    return Boolean.FALSE;
                }
                Boolean r2 = (Boolean) nodes[RIGHT].getValue(session);
                if (Boolean.FALSE.equals(r2)) {
                    return Boolean.FALSE;
                }
                if (r1 == null || r2 == null) {
                    return null;
                }
                return Boolean.TRUE;
            }
        case OpTypes.OR:
            {
                Boolean r1 = (Boolean) nodes[LEFT].getValue(session);
                if (Boolean.TRUE.equals(r1)) {
                    return Boolean.TRUE;
                }
                Boolean r2 = (Boolean) nodes[RIGHT].getValue(session);
                if (Boolean.TRUE.equals(r2)) {
                    return Boolean.TRUE;
                }
                if (r1 == null || r2 == null) {
                    return null;
                }
                return Boolean.FALSE;
            }
        case OpTypes.NOT_DISTINCT:
        case OpTypes.EQUAL:
        case OpTypes.GREATER:
        case OpTypes.GREATER_EQUAL:
        case OpTypes.SMALLER_EQUAL:
        case OpTypes.SMALLER:
        case OpTypes.NOT_EQUAL:
            {
                if (exprSubType == OpTypes.ANY_QUANTIFIED || exprSubType == OpTypes.ALL_QUANTIFIED) {
                    return testAllAnyCondition(session, (Object[]) nodes[LEFT].getRowValue(session));
                }
                Object o1 = nodes[LEFT].getValue(session);
                Object o2 = nodes[RIGHT].getValue(session);
                if (o1 instanceof Object[]) {
                    if (!(o2 instanceof Object[])) {
                        throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionLogical");
                    }
                    return compareValues(session, (Object[]) o1, (Object[]) o2);
                } else {
                    if (o2 instanceof Object[]) {
                        o2 = ((Object[]) o2)[0];
                    }
                    return compareValues(session, o1, o2);
                }
            }
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionLogical");
    }
}
