void resolveTypesForAllAny(Session session) {
    int degree = nodes[LEFT].getDegree();
    if (degree == 1 && nodes[LEFT].opType != OpTypes.ROW) {
        nodes[LEFT] = new Expression(OpTypes.ROW, new Expression[] { nodes[LEFT] });
    }
    if (nodes[RIGHT].opType == OpTypes.TABLE) {
        nodes[RIGHT].prepareTable(session, nodes[LEFT], degree);
        nodes[RIGHT].subQuery.prepareTable(session);
        if (nodes[RIGHT].isCorrelated) {
            nodes[RIGHT].subQuery.setCorrelated();
        }
    }
    if (degree != nodes[RIGHT].nodeDataTypes.length) {
        throw Error.error(ErrorCode.X_42564);
    }
    if (nodes[RIGHT].opType == OpTypes.TABLE) {
    }
    nodes[LEFT].nodeDataTypes = new Type[nodes[LEFT].nodes.length];
    for (int i = 0; i < nodes[LEFT].nodeDataTypes.length; i++) {
        Type type = nodes[LEFT].nodes[i].dataType;
        if (type == null) {
            type = nodes[RIGHT].nodeDataTypes[i];
        }
        if (type == null) {
            throw Error.error(ErrorCode.X_42567);
        }
        nodes[LEFT].nodeDataTypes[i] = type;
        nodes[LEFT].nodes[i].dataType = type;
    }
}
