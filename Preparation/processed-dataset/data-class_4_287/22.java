/**
     * Sets the types of all the expressions used in this SELECT list.
     */
public void resolveExpressionTypes(Session session, Expression parent) {
    for (int i = 0; i < indexStartAggregates; i++) {
        Expression e = exprColumns[i];
        e.resolveTypes(session, parent);
        if (e.getType() == OpTypes.ROW) {
            throw Error.error(ErrorCode.X_42564);
        }
    }
    for (int i = 0, len = rangeVariables.length; i < len; i++) {
        Expression e = rangeVariables[i].getJoinCondition();
        if (e != null) {
            e.resolveTypes(session, null);
            if (e.getDataType() != Type.SQL_BOOLEAN) {
                throw Error.error(ErrorCode.X_42568);
            }
        }
    }
    if (queryCondition != null) {
        queryCondition.resolveTypes(session, null);
        if (queryCondition.getDataType() != Type.SQL_BOOLEAN) {
            throw Error.error(ErrorCode.X_42568);
        }
    }
    if (havingCondition != null) {
        havingCondition.resolveTypes(session, null);
        if (havingCondition.getDataType() != Type.SQL_BOOLEAN) {
            throw Error.error(ErrorCode.X_42568);
        }
    }
}
