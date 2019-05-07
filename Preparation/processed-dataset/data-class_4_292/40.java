void prepareTable(Session session, Expression row, int degree) {
    if (nodeDataTypes != null) {
        return;
    }
    for (int i = 0; i < nodes.length; i++) {
        Expression e = nodes[i];
        if (e.opType == OpTypes.ROW) {
            if (degree != e.nodes.length) {
                throw Error.error(ErrorCode.X_42564);
            }
        } else if (degree == 1) {
            nodes[i] = new Expression(OpTypes.ROW);
            nodes[i].nodes = new Expression[] { e };
        } else {
            throw Error.error(ErrorCode.X_42564);
        }
    }
    nodeDataTypes = new Type[degree];
    for (int j = 0; j < degree; j++) {
        Type type = row == null ? null : row.nodes[j].dataType;
        for (int i = 0; i < nodes.length; i++) {
            type = Type.getAggregateType(nodes[i].nodes[j].dataType, type);
        }
        if (type == null) {
            throw Error.error(ErrorCode.X_42567);
        }
        nodeDataTypes[j] = type;
        if (row != null && row.nodes[j].isUnresolvedParam()) {
            row.nodes[j].dataType = type;
        }
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].nodes[j].isUnresolvedParam()) {
                nodes[i].nodes[j].dataType = nodeDataTypes[j];
                continue;
            }
            if (nodes[i].nodes[j].opType == OpTypes.VALUE) {
                if (nodes[i].nodes[j].valueData == null) {
                    nodes[i].nodes[j].dataType = nodeDataTypes[j];
                }
            }
        }
        if (nodeDataTypes[j].isCharacterType() && !((CharacterType) nodeDataTypes[j]).isEqualIdentical()) {
        }
    }
}
