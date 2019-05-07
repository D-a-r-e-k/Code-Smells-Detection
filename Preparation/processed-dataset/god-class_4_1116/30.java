boolean reorderComparison(Session session) {
    Expression colExpression = null;
    Expression nonColExpression = null;
    boolean left = false;
    boolean replaceColumn = false;
    int operation = 0;
    if (nodes[LEFT].opType == OpTypes.ADD) {
        operation = OpTypes.SUBTRACT;
        left = true;
    } else if (nodes[LEFT].opType == OpTypes.SUBTRACT) {
        operation = OpTypes.ADD;
        left = true;
    } else if (nodes[RIGHT].opType == OpTypes.ADD) {
        operation = OpTypes.SUBTRACT;
    } else if (nodes[RIGHT].opType == OpTypes.SUBTRACT) {
        operation = OpTypes.ADD;
    }
    if (operation == 0) {
        return false;
    }
    if (left) {
        if (nodes[LEFT].nodes[LEFT].opType == OpTypes.COLUMN) {
            colExpression = nodes[LEFT].nodes[LEFT];
            nonColExpression = nodes[LEFT].nodes[RIGHT];
        } else if (nodes[LEFT].nodes[RIGHT].opType == OpTypes.COLUMN) {
            replaceColumn = operation == OpTypes.ADD;
            colExpression = nodes[LEFT].nodes[RIGHT];
            nonColExpression = nodes[LEFT].nodes[LEFT];
        }
    } else {
        if (nodes[RIGHT].nodes[LEFT].opType == OpTypes.COLUMN) {
            colExpression = nodes[RIGHT].nodes[LEFT];
            nonColExpression = nodes[RIGHT].nodes[RIGHT];
        } else if (nodes[RIGHT].nodes[RIGHT].opType == OpTypes.COLUMN) {
            replaceColumn = operation == OpTypes.ADD;
            colExpression = nodes[RIGHT].nodes[RIGHT];
            nonColExpression = nodes[RIGHT].nodes[LEFT];
        }
    }
    if (colExpression == null) {
        return false;
    }
    Expression otherExpression = left ? nodes[RIGHT] : nodes[LEFT];
    ExpressionArithmetic newArg = null;
    if (!replaceColumn) {
        newArg = new ExpressionArithmetic(operation, otherExpression, nonColExpression);
        newArg.resolveTypesForArithmetic(session);
    }
    if (left) {
        if (replaceColumn) {
            nodes[RIGHT] = colExpression;
            nodes[LEFT].nodes[RIGHT] = otherExpression;
            ((ExpressionArithmetic) nodes[LEFT]).resolveTypesForArithmetic(session);
        } else {
            nodes[LEFT] = colExpression;
            nodes[RIGHT] = newArg;
        }
    } else {
        if (replaceColumn) {
            nodes[LEFT] = colExpression;
            nodes[RIGHT].nodes[RIGHT] = otherExpression;
            ((ExpressionArithmetic) nodes[RIGHT]).resolveTypesForArithmetic(session);
        } else {
            nodes[RIGHT] = colExpression;
            nodes[LEFT] = newArg;
        }
    }
    return true;
}
