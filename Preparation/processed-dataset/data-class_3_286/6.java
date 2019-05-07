void readSetClauseList(RangeVariable[] rangeVars, OrderedHashSet targets, LongDeque colIndexList, HsqlArrayList expressions) {
    while (true) {
        int degree;
        if (token.tokenType == Tokens.OPENBRACKET) {
            read();
            int oldCount = targets.size();
            readTargetSpecificationList(targets, rangeVars, colIndexList);
            degree = targets.size() - oldCount;
            readThis(Tokens.CLOSEBRACKET);
        } else {
            Expression target = XreadTargetSpecification(rangeVars, colIndexList);
            if (!targets.add(target)) {
                ColumnSchema col = target.getColumn();
                throw Error.error(ErrorCode.X_42579, col.getName().name);
            }
            degree = 1;
        }
        readThis(Tokens.EQUALS);
        int position = getPosition();
        int brackets = readOpenBrackets();
        if (token.tokenType == Tokens.SELECT) {
            rewind(position);
            SubQuery sq = XreadSubqueryBody(false, OpTypes.ROW_SUBQUERY);
            if (degree != sq.queryExpression.getColumnCount()) {
                throw Error.error(ErrorCode.X_42546);
            }
            Expression e = new Expression(OpTypes.ROW_SUBQUERY, sq);
            expressions.add(e);
            if (token.tokenType == Tokens.COMMA) {
                read();
                continue;
            }
            break;
        }
        if (brackets > 0) {
            rewind(position);
        }
        if (degree > 1) {
            readThis(Tokens.OPENBRACKET);
            Expression e = readRow();
            readThis(Tokens.CLOSEBRACKET);
            int rowDegree = e.getType() == OpTypes.ROW ? e.nodes.length : 1;
            if (degree != rowDegree) {
                throw Error.error(ErrorCode.X_42546);
            }
            expressions.add(e);
        } else {
            Expression e = XreadValueExpressionWithContext();
            expressions.add(e);
        }
        if (token.tokenType == Tokens.COMMA) {
            read();
            continue;
        }
        break;
    }
}
