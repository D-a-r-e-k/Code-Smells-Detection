void mergeQuery() {
    RangeVariable rangeVar = rangeVariables[0];
    Table table = rangeVar.getTable();
    Expression localQueryCondition = queryCondition;
    if (table instanceof TableDerived) {
        QueryExpression baseQueryExpression = ((TableDerived) table).getQueryExpression();
        if (baseQueryExpression == null || !baseQueryExpression.isMergeable) {
            isMergeable = false;
            return;
        }
        QuerySpecification baseSelect = baseQueryExpression.getMainSelect();
        if (baseQueryExpression.view == null) {
            rangeVariables[0] = baseSelect.rangeVariables[0];
            rangeVariables[0].resetConditions();
            Expression[] newExprColumns = new Expression[indexLimitData];
            for (int i = 0; i < indexLimitData; i++) {
                Expression e = exprColumns[i];
                newExprColumns[i] = e.replaceColumnReferences(rangeVar, baseSelect.exprColumns);
            }
            exprColumns = newExprColumns;
            if (localQueryCondition != null) {
                localQueryCondition = localQueryCondition.replaceColumnReferences(rangeVar, baseSelect.exprColumns);
            }
            Expression baseQueryCondition = baseSelect.queryCondition;
            checkQueryCondition = baseSelect.checkQueryCondition;
            queryCondition = ExpressionLogical.andExpressions(baseQueryCondition, localQueryCondition);
        } else {
            RangeVariable[] newRangeVariables = new RangeVariable[1];
            newRangeVariables[0] = baseSelect.rangeVariables[0].duplicate();
            Expression[] newBaseExprColumns = new Expression[baseSelect.indexLimitData];
            for (int i = 0; i < baseSelect.indexLimitData; i++) {
                Expression e = baseSelect.exprColumns[i].duplicate();
                newBaseExprColumns[i] = e;
                e.replaceRangeVariables(baseSelect.rangeVariables, newRangeVariables);
            }
            for (int i = 0; i < indexLimitData; i++) {
                Expression e = exprColumns[i];
                exprColumns[i] = e.replaceColumnReferences(rangeVar, newBaseExprColumns);
            }
            Expression baseQueryCondition = baseSelect.queryCondition;
            if (baseQueryCondition != null) {
                baseQueryCondition = baseQueryCondition.duplicate();
                baseQueryCondition.replaceRangeVariables(baseSelect.rangeVariables, newRangeVariables);
            }
            if (localQueryCondition != null) {
                localQueryCondition = localQueryCondition.replaceColumnReferences(rangeVar, newBaseExprColumns);
            }
            checkQueryCondition = baseSelect.checkQueryCondition;
            if (checkQueryCondition != null) {
                checkQueryCondition = checkQueryCondition.duplicate();
                checkQueryCondition.replaceRangeVariables(baseSelect.rangeVariables, newRangeVariables);
            }
            queryCondition = ExpressionLogical.andExpressions(baseQueryCondition, localQueryCondition);
            rangeVariables = newRangeVariables;
        }
    }
    if (view != null) {
        switch(view.getCheckOption()) {
            case SchemaObject.ViewCheckModes.CHECK_LOCAL:
                if (!isUpdatable) {
                    throw Error.error(ErrorCode.X_42537);
                }
                checkQueryCondition = localQueryCondition;
                break;
            case SchemaObject.ViewCheckModes.CHECK_CASCADE:
                if (!isUpdatable) {
                    throw Error.error(ErrorCode.X_42537);
                }
                checkQueryCondition = queryCondition;
                break;
        }
    }
    if (isAggregated) {
        isMergeable = false;
    }
}
