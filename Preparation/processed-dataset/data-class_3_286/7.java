/**
     * Retrieves a MERGE Statement from this parse context.
     */
StatementDMQL compileMergeStatement(RangeVariable[] outerRanges) {
    boolean[] insertColumnCheckList;
    int[] insertColumnMap = null;
    int[] updateColumnMap = null;
    int[] baseUpdateColumnMap;
    Table table;
    RangeVariable targetRange;
    RangeVariable sourceRange;
    Expression mergeCondition;
    Expression[] targets = null;
    HsqlArrayList updateList = new HsqlArrayList();
    Expression[] updateExpressions = Expression.emptyArray;
    HsqlArrayList insertList = new HsqlArrayList();
    Expression insertExpression = null;
    read();
    readThis(Tokens.INTO);
    targetRange = readSimpleRangeVariable(StatementTypes.MERGE);
    table = targetRange.rangeTable;
    readThis(Tokens.USING);
    sourceRange = readTableOrSubquery();
    // parse ON search conditions 
    readThis(Tokens.ON);
    mergeCondition = XreadBooleanValueExpression();
    if (mergeCondition.getDataType() != Type.SQL_BOOLEAN) {
        throw Error.error(ErrorCode.X_42568);
    }
    RangeVariable[] fullRangeVars = new RangeVariable[] { sourceRange, targetRange };
    RangeVariable[] sourceRangeVars = new RangeVariable[] { sourceRange };
    RangeVariable[] targetRangeVars = new RangeVariable[] { targetRange };
    // parse WHEN clause(s) and convert lists to arrays 
    insertColumnMap = table.getColumnMap();
    insertColumnCheckList = table.getNewColumnCheckList();
    OrderedHashSet updateTargetSet = new OrderedHashSet();
    OrderedHashSet insertColNames = new OrderedHashSet();
    LongDeque updateColIndexList = new LongDeque();
    readMergeWhen(updateColIndexList, insertColNames, updateTargetSet, insertList, updateList, targetRangeVars, sourceRange);
    if (insertList.size() > 0) {
        int colCount = insertColNames.size();
        if (colCount != 0) {
            insertColumnMap = table.getColumnIndexes(insertColNames);
            insertColumnCheckList = table.getColumnCheckList(insertColumnMap);
        }
        insertExpression = (Expression) insertList.get(0);
        setParameterTypes(insertExpression, table, insertColumnMap);
    }
    if (updateList.size() > 0) {
        targets = new Expression[updateTargetSet.size()];
        updateTargetSet.toArray(targets);
        for (int i = 0; i < targets.length; i++) {
            this.resolveOuterReferencesAndTypes(outerRanges, targets[i]);
        }
        updateExpressions = new Expression[updateList.size()];
        updateList.toArray(updateExpressions);
        updateColumnMap = new int[updateColIndexList.size()];
        updateColIndexList.toArray(updateColumnMap);
    }
    if (updateExpressions.length != 0) {
        Table baseTable = table.getBaseTable();
        baseUpdateColumnMap = updateColumnMap;
        if (table != baseTable) {
            baseUpdateColumnMap = new int[updateColumnMap.length];
            ArrayUtil.projectRow(table.getBaseTableColumnMap(), updateColumnMap, baseUpdateColumnMap);
        }
        resolveUpdateExpressions(table, sourceRangeVars, updateColumnMap, updateExpressions, outerRanges);
    }
    HsqlList unresolved = null;
    unresolved = mergeCondition.resolveColumnReferences(fullRangeVars, null);
    ExpressionColumn.checkColumnsResolved(unresolved);
    mergeCondition.resolveTypes(session, null);
    if (mergeCondition.isUnresolvedParam()) {
        mergeCondition.dataType = Type.SQL_BOOLEAN;
    }
    if (mergeCondition.getDataType() != Type.SQL_BOOLEAN) {
        throw Error.error(ErrorCode.X_42568);
    }
    RangeVariableResolver resolver = new RangeVariableResolver(fullRangeVars, mergeCondition, compileContext);
    resolver.processConditions(session);
    fullRangeVars = resolver.rangeVariables;
    if (insertExpression != null) {
        unresolved = insertExpression.resolveColumnReferences(sourceRangeVars, unresolved);
        ExpressionColumn.checkColumnsResolved(unresolved);
        insertExpression.resolveTypes(session, null);
    }
    StatementDMQL cs = new StatementDML(session, targets, fullRangeVars, insertColumnMap, updateColumnMap, insertColumnCheckList, mergeCondition, insertExpression, updateExpressions, compileContext);
    return cs;
}
