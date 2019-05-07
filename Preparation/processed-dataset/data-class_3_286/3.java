/**
     * Creates a DELETE-type Statement from this parse context.
     */
StatementDMQL compileDeleteStatement(RangeVariable[] outerRanges) {
    Expression condition = null;
    boolean truncate = false;
    boolean restartIdentity = false;
    int statementType;
    switch(token.tokenType) {
        case Tokens.TRUNCATE:
            {
                read();
                readThis(Tokens.TABLE);
                truncate = true;
                statementType = StatementTypes.TRUNCATE;
                break;
            }
        case Tokens.DELETE:
            {
                read();
                readThis(Tokens.FROM);
                statementType = StatementTypes.DELETE_WHERE;
                break;
            }
        default:
            throw unexpectedToken();
    }
    RangeVariable[] rangeVariables = { readSimpleRangeVariable(statementType) };
    Table table = rangeVariables[0].getTable();
    Table baseTable = table.getBaseTable();
    if (truncate) {
        if (table != baseTable) {
            throw Error.error(ErrorCode.X_42545);
        }
        if (table.isTriggerDeletable()) {
            // redundant 
            throw Error.error(ErrorCode.X_42545);
        }
        switch(token.tokenType) {
            case Tokens.CONTINUE:
                {
                    read();
                    readThis(Tokens.IDENTITY);
                    break;
                }
            case Tokens.RESTART:
                {
                    read();
                    readThis(Tokens.IDENTITY);
                    restartIdentity = true;
                    break;
                }
        }
        if (table.fkMainConstraints.length > 0) {
            throw Error.error(ErrorCode.X_23504);
        }
    }
    if (!truncate && token.tokenType == Tokens.WHERE) {
        read();
        condition = XreadBooleanValueExpression();
        HsqlList unresolved = condition.resolveColumnReferences(outerRanges, null);
        unresolved = Expression.resolveColumnSet(rangeVariables, rangeVariables.length, unresolved, null);
        ExpressionColumn.checkColumnsResolved(unresolved);
        condition.resolveTypes(session, null);
        if (condition.isUnresolvedParam()) {
            condition.dataType = Type.SQL_BOOLEAN;
        }
        if (condition.getDataType() != Type.SQL_BOOLEAN) {
            throw Error.error(ErrorCode.X_42568);
        }
    }
    if (baseTable == null) {
    } else if (table != baseTable) {
        QuerySpecification baseSelect = ((TableDerived) table).getQueryExpression().getMainSelect();
        RangeVariable[] newRangeVariables = (RangeVariable[]) ArrayUtil.duplicateArray(baseSelect.rangeVariables);
        newRangeVariables[0] = baseSelect.rangeVariables[0].duplicate();
        Expression[] newBaseExprColumns = new Expression[baseSelect.indexLimitData];
        for (int i = 0; i < baseSelect.indexLimitData; i++) {
            Expression e = baseSelect.exprColumns[i].duplicate();
            newBaseExprColumns[i] = e;
            e.replaceRangeVariables(baseSelect.rangeVariables, newRangeVariables);
        }
        Expression baseQueryCondition = baseSelect.queryCondition;
        if (baseQueryCondition != null) {
            baseQueryCondition = baseQueryCondition.duplicate();
            baseQueryCondition.replaceRangeVariables(rangeVariables, newRangeVariables);
        }
        if (condition != null) {
            condition = condition.replaceColumnReferences(rangeVariables[0], newBaseExprColumns);
        }
        rangeVariables = newRangeVariables;
        condition = ExpressionLogical.andExpressions(baseQueryCondition, condition);
    }
    if (condition != null) {
        rangeVariables[0].addJoinCondition(condition);
        RangeVariableResolver resolver = new RangeVariableResolver(rangeVariables, null, compileContext);
        resolver.processConditions(session);
        rangeVariables = resolver.rangeVariables;
    }
    StatementDMQL cs = new StatementDML(session, table, rangeVariables, compileContext, restartIdentity, statementType);
    return cs;
}
