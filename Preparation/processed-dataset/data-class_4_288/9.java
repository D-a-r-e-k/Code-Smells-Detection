private void resolveTypesForComparison(Session session, Expression parent) {
    if (opType == OpTypes.NOT_DISTINCT || exprSubType == OpTypes.ALL_QUANTIFIED || exprSubType == OpTypes.ANY_QUANTIFIED) {
        resolveTypesForAllAny(session);
        checkRowComparison();
        return;
    }
    if (nodes[LEFT].opType == OpTypes.ROW || nodes[RIGHT].opType == OpTypes.ROW) {
        if (nodes[LEFT].opType != OpTypes.ROW || nodes[RIGHT].opType != OpTypes.ROW || nodes[LEFT].nodes.length != nodes[RIGHT].nodes.length) {
            throw Error.error(ErrorCode.X_42564);
        }
        resolveRowTypes();
        checkRowComparison();
        return;
    } else {
        if (nodes[LEFT].isUnresolvedParam()) {
            nodes[LEFT].dataType = nodes[RIGHT].dataType;
        } else if (nodes[RIGHT].isUnresolvedParam()) {
            nodes[RIGHT].dataType = nodes[LEFT].dataType;
        }
        if (nodes[LEFT].dataType == null) {
            nodes[LEFT].dataType = nodes[RIGHT].dataType;
        } else if (nodes[RIGHT].dataType == null) {
            nodes[RIGHT].dataType = nodes[LEFT].dataType;
        }
        if (nodes[LEFT].dataType == null || nodes[RIGHT].dataType == null) {
            throw Error.error(ErrorCode.X_42567);
        }
        if (nodes[LEFT].dataType.typeComparisonGroup != nodes[RIGHT].dataType.typeComparisonGroup) {
            if (convertDateTimeLiteral(session, nodes[LEFT], nodes[RIGHT])) {
            } else if (nodes[LEFT].dataType.isBitType()) {
                if (nodes[RIGHT].dataType.canConvertFrom(nodes[LEFT].dataType)) {
                    nodes[LEFT] = ExpressionOp.getCastExpression(session, nodes[LEFT], nodes[RIGHT].dataType);
                }
            } else if (nodes[RIGHT].dataType.isBitType()) {
                if (nodes[LEFT].dataType.canConvertFrom(nodes[RIGHT].dataType)) {
                    nodes[RIGHT] = ExpressionOp.getCastExpression(session, nodes[RIGHT], nodes[LEFT].dataType);
                }
            } else {
                throw Error.error(ErrorCode.X_42562);
            }
        } else if (nodes[LEFT].dataType.isDateTimeType()) {
            if (nodes[LEFT].dataType.isDateTimeTypeWithZone() ^ nodes[RIGHT].dataType.isDateTimeTypeWithZone()) {
                nodes[LEFT] = new ExpressionOp(nodes[LEFT]);
            }
        }
        if (opType == OpTypes.EQUAL || opType == OpTypes.NOT_EQUAL) {
        } else {
            if (nodes[LEFT].dataType.isArrayType() || nodes[LEFT].dataType.isLobType() || nodes[RIGHT].dataType.isLobType()) {
                throw Error.error(ErrorCode.X_42534);
            }
        }
        if (nodes[LEFT].opType == OpTypes.VALUE && nodes[RIGHT].opType == OpTypes.VALUE) {
            setAsConstantValue(session);
        }
    }
}
