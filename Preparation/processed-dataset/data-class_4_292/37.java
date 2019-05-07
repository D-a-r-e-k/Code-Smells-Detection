public void resolveTypes(Session session, Expression parent) {
    for (int i = 0; i < nodes.length; i++) {
        if (nodes[i] != null) {
            nodes[i].resolveTypes(session, this);
        }
    }
    switch(opType) {
        case OpTypes.VALUE:
            break;
        case OpTypes.TABLE:
            /** @todo - should it fall through */
            break;
        case OpTypes.ROW:
            nodeDataTypes = new Type[nodes.length];
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i] != null) {
                    nodeDataTypes[i] = nodes[i].dataType;
                }
            }
            break;
        case OpTypes.ARRAY:
            {
                boolean hasUndefined = false;
                for (int i = 0; i < nodes.length; i++) {
                    if (nodes[i].dataType == null) {
                        hasUndefined = true;
                    } else {
                        dataType = Type.getAggregateType(dataType, nodes[i].dataType);
                    }
                }
                if (hasUndefined) {
                    for (int i = 0; i < nodes.length; i++) {
                        if (nodes[i].dataType == null) {
                            nodes[i].dataType = dataType;
                        }
                    }
                }
                dataType = new ArrayType(dataType, nodes.length);
                return;
            }
        case OpTypes.ARRAY_SUBQUERY:
            {
                QueryExpression queryExpression = subQuery.queryExpression;
                queryExpression.resolveTypes(session);
                subQuery.prepareTable(session);
                nodeDataTypes = queryExpression.getColumnTypes();
                dataType = nodeDataTypes[0];
                if (nodeDataTypes.length > 1) {
                    throw Error.error(ErrorCode.X_42564);
                }
                dataType = new ArrayType(dataType, nodes.length);
                break;
            }
        case OpTypes.ROW_SUBQUERY:
        case OpTypes.TABLE_SUBQUERY:
            {
                QueryExpression queryExpression = subQuery.queryExpression;
                queryExpression.resolveTypes(session);
                subQuery.prepareTable(session);
                nodeDataTypes = queryExpression.getColumnTypes();
                dataType = nodeDataTypes[0];
                break;
            }
        default:
            throw Error.runtimeError(ErrorCode.U_S0500, "Expression");
    }
}
