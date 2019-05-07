/**
     * Creates an UPDATE-type Statement from this parse context.
     */
StatementDMQL compileUpdateStatement(RangeVariable[] outerRanges) {
    read();
    Expression[] updateExpressions;
    int[] columnMap;
    boolean[] columnCheckList;
    OrderedHashSet targetSet = new OrderedHashSet();
    LongDeque colIndexList = new LongDeque();
    HsqlArrayList exprList = new HsqlArrayList();
    RangeVariable[] rangeVariables = { readSimpleRangeVariable(StatementTypes.UPDATE_WHERE) };
    Table table = rangeVariables[0].rangeTable;
    Table baseTable = table.getBaseTable();
    readThis(Tokens.SET);
    readSetClauseList(rangeVariables, targetSet, colIndexList, exprList);
    columnMap = new int[colIndexList.size()];
    colIndexList.toArray(columnMap);
    Expression[] targets = new Expression[targetSet.size()];
    targetSet.toArray(targets);
    for (int i = 0; i < targets.length; i++) {
        this.resolveOuterReferencesAndTypes(outerRanges, targets[i]);
    }
    columnCheckList = table.getColumnCheckList(columnMap);
    updateExpressions = new Expression[exprList.size()];
    exprList.toArray(updateExpressions);
    Expression condition = null;
    if (token.tokenType == Tokens.WHERE) {
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
    resolveUpdateExpressions(table, rangeVariables, columnMap, updateExpressions, outerRanges);
    if (table != baseTable) {
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
        for (int i = 0; i < updateExpressions.length; i++) {
            updateExpressions[i] = updateExpressions[i].replaceColumnReferences(rangeVariables[0], newBaseExprColumns);
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
    if (baseTable != null && table != baseTable) {
        int[] baseColumnMap = table.getBaseTableColumnMap();
        int[] newColumnMap = new int[columnMap.length];
        ArrayUtil.projectRow(baseColumnMap, columnMap, newColumnMap);
        columnMap = newColumnMap;
        for (int i = 0; i < columnMap.length; i++) {
            if (baseTable.colGenerated[columnMap[i]]) {
                throw Error.error(ErrorCode.X_42513);
            }
        }
    }
    StatementDMQL cs = new StatementDML(session, targets, table, rangeVariables, columnMap, updateExpressions, columnCheckList, compileContext);
    return cs;
}
