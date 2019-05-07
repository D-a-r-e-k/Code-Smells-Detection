Expression getIndexableExpression(RangeVariable rangeVar) {
    switch(opType) {
        case OpTypes.IS_NULL:
            return nodes[LEFT].opType == OpTypes.COLUMN && nodes[LEFT].isIndexable(rangeVar) ? this : null;
        case OpTypes.NOT:
            return nodes[LEFT].opType == OpTypes.IS_NULL && nodes[LEFT].nodes[LEFT].opType == OpTypes.COLUMN && nodes[LEFT].nodes[LEFT].isIndexable(rangeVar) ? this : null;
        case OpTypes.EQUAL:
            if (exprSubType == OpTypes.ANY_QUANTIFIED) {
                if (nodes[RIGHT].isCorrelated) {
                    return null;
                }
                for (int node = 0; node < nodes[LEFT].nodes.length; node++) {
                    if (nodes[LEFT].nodes[node].opType == OpTypes.COLUMN && nodes[LEFT].nodes[node].isIndexable(rangeVar)) {
                        return this;
                    }
                }
                return null;
            }
        // fall through 
        case OpTypes.GREATER:
        case OpTypes.GREATER_EQUAL:
        case OpTypes.SMALLER:
        case OpTypes.SMALLER_EQUAL:
            if (exprSubType != 0) {
                return null;
            }
            if (nodes[LEFT].opType == OpTypes.COLUMN && nodes[LEFT].isIndexable(rangeVar)) {
                if (nodes[RIGHT].hasReference(rangeVar)) {
                    return null;
                }
                return this;
            }
            if (nodes[LEFT].hasReference(rangeVar)) {
                return null;
            }
            if (nodes[RIGHT].opType == OpTypes.COLUMN && nodes[RIGHT].isIndexable(rangeVar)) {
                swapCondition();
                return this;
            }
            return null;
        case OpTypes.OR:
            if (isIndexable(rangeVar)) {
                return this;
            }
            return null;
        default:
            return null;
    }
}
