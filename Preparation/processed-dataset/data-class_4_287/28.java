void resolveTypesPartTwo(Session session) {
    resolveGroups();
    for (int i = 0; i < unionColumnTypes.length; i++) {
        Type type = unionColumnTypes[i];
        if (type == null) {
            throw Error.error(ErrorCode.X_42567);
        }
        exprColumns[i].setDataType(session, type);
    }
    for (int i = 0; i < indexStartHaving; i++) {
        if (exprColumns[i].dataType == null) {
            throw Error.error(ErrorCode.X_42567);
        }
    }
    checkLobUsage();
    setMergeability();
    setUpdatability();
    createResultMetaData();
    createTable(session);
    if (isMergeable) {
        mergeQuery();
    }
    setRangeVariableConditions(session);
    if (isAggregated && !isGrouped && !sortAndSlice.hasOrder() && !sortAndSlice.hasLimit() && aggregateSet.size() == 1 && indexLimitVisible == 1) {
        Expression e = exprColumns[indexStartAggregates];
        int opType = e.getType();
        switch(opType) {
            case OpTypes.MAX:
            case OpTypes.MIN:
                {
                    SortAndSlice slice = new SortAndSlice();
                    slice.isGenerated = true;
                    slice.addLimitCondition(ExpressionOp.limitOneExpression);
                    if (slice.prepareSpecial(session, this)) {
                        this.sortAndSlice = slice;
                    }
                    break;
                }
            case OpTypes.COUNT:
                {
                    if (rangeVariables.length == 1 && queryCondition == null && e.getLeftNode().getType() == OpTypes.ASTERISK) {
                        isSimpleCount = true;
                    }
                }
        }
    }
    sortAndSlice.setSortRange(this);
    isResolved = true;
}
