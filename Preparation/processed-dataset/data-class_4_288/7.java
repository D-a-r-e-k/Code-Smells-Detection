public void resolveTypes(Session session, Expression parent) {
    // parametric ALL / ANY 
    if (isQuantified) {
        if (nodes[RIGHT].opType == OpTypes.TABLE) {
            if (nodes[RIGHT] instanceof ExpressionTable) {
                if (nodes[RIGHT].nodes[LEFT].opType == OpTypes.DYNAMIC_PARAM) {
                    nodes[LEFT].resolveTypes(session, this);
                    nodes[RIGHT].nodes[LEFT].dataType = Type.getDefaultArrayType(nodes[LEFT].dataType.typeCode);
                }
            }
        }
    }
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
            nodes[i].resolveTypes(session, this);
        }
    }
    switch(opType) {
        case OpTypes.VALUE:
            break;
        case OpTypes.NOT_DISTINCT:
        case OpTypes.EQUAL:
        case OpTypes.GREATER_EQUAL:
        case OpTypes.GREATER:
        case OpTypes.SMALLER:
        case OpTypes.SMALLER_EQUAL:
        case OpTypes.NOT_EQUAL:
            resolveTypesForComparison(session, parent);
            break;
        case OpTypes.AND:
            {
                resolveTypesForLogicalOp();
                if (nodes[LEFT].opType == OpTypes.VALUE) {
                    if (nodes[RIGHT].opType == OpTypes.VALUE) {
                        setAsConstantValue(session);
                    } else {
                        Object value = nodes[LEFT].getValue(session);
                        if (value == null || Boolean.FALSE.equals(value)) {
                            setAsConstantValue(Boolean.FALSE);
                        }
                    }
                } else if (nodes[RIGHT].opType == OpTypes.VALUE) {
                    Object value = nodes[RIGHT].getValue(session);
                    if (value == null || Boolean.FALSE.equals(value)) {
                        setAsConstantValue(Boolean.FALSE);
                    }
                }
                break;
            }
        case OpTypes.OR:
            {
                resolveTypesForLogicalOp();
                if (nodes[LEFT].opType == OpTypes.VALUE) {
                    if (nodes[RIGHT].opType == OpTypes.VALUE) {
                        setAsConstantValue(session);
                    } else {
                        Object value = nodes[LEFT].getValue(session);
                        if (Boolean.TRUE.equals(value)) {
                            setAsConstantValue(Boolean.TRUE);
                        }
                    }
                } else if (nodes[RIGHT].opType == OpTypes.VALUE) {
                    Object value = nodes[RIGHT].getValue(session);
                    if (Boolean.TRUE.equals(value)) {
                        setAsConstantValue(Boolean.TRUE);
                    }
                }
                break;
            }
        case OpTypes.IS_NULL:
            if (nodes[LEFT].isUnresolvedParam()) {
                throw Error.error(ErrorCode.X_42563);
            }
            if (nodes[LEFT].opType == OpTypes.VALUE) {
                setAsConstantValue(session);
            }
            break;
        case OpTypes.NOT:
            if (nodes[LEFT].isUnresolvedParam()) {
                nodes[LEFT].dataType = Type.SQL_BOOLEAN;
                break;
            }
            if (nodes[LEFT].opType == OpTypes.VALUE) {
                if (nodes[LEFT].dataType.isBooleanType()) {
                    setAsConstantValue(session);
                    break;
                } else {
                    throw Error.error(ErrorCode.X_42563);
                }
            }
            if (nodes[LEFT].dataType == null || !nodes[LEFT].dataType.isBooleanType()) {
                throw Error.error(ErrorCode.X_42563);
            }
            dataType = Type.SQL_BOOLEAN;
            break;
        case OpTypes.OVERLAPS:
            resolveTypesForOverlaps();
            break;
        case OpTypes.IN:
            resolveTypesForIn(session);
            break;
        case OpTypes.MATCH_SIMPLE:
        case OpTypes.MATCH_PARTIAL:
        case OpTypes.MATCH_FULL:
        case OpTypes.MATCH_UNIQUE_SIMPLE:
        case OpTypes.MATCH_UNIQUE_PARTIAL:
        case OpTypes.MATCH_UNIQUE_FULL:
            resolveTypesForAllAny(session);
            break;
        case OpTypes.UNIQUE:
        case OpTypes.EXISTS:
            break;
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "ExpressionLogical");
    }
}
