/**
     * Executes a MERGE statement.
     *
     * @return Result object
     */
Result executeMergeStatement(Session session) {
    Type[] colTypes = baseTable.getColumnTypes();
    Result resultOut = null;
    RowSetNavigator generatedNavigator = null;
    if (generatedIndexes != null) {
        resultOut = Result.newUpdateCountResult(generatedResultMetaData, 0);
        generatedNavigator = resultOut.getChainedResult().getNavigator();
    }
    int count = 0;
    // data generated for non-matching rows 
    RowSetNavigatorClient newData = new RowSetNavigatorClient(8);
    // rowset for update operation 
    RowSetNavigatorDataChange updateRowSet = new RowSetNavigatorDataChange();
    RangeVariable[] joinRangeIterators = targetRangeVariables;
    // populate insert and update lists 
    RangeIterator[] rangeIterators = new RangeIterator[joinRangeIterators.length];
    for (int i = 0; i < joinRangeIterators.length; i++) {
        rangeIterators[i] = joinRangeIterators[i].getIterator(session);
    }
    for (int currentIndex = 0; currentIndex >= 0; ) {
        RangeIterator it = rangeIterators[currentIndex];
        boolean beforeFirst = it.isBeforeFirst();
        if (it.next()) {
            if (currentIndex < joinRangeIterators.length - 1) {
                currentIndex++;
                continue;
            }
        } else {
            if (currentIndex == 1 && beforeFirst && insertExpression != null) {
                Object[] data = getInsertData(session, colTypes, insertExpression.nodes[0].nodes);
                if (data != null) {
                    newData.add(data);
                }
            }
            it.reset();
            currentIndex--;
            continue;
        }
        // row matches! 
        if (updateExpressions.length != 0) {
            Row row = it.getCurrentRow();
            // this is always the second iterator 
            Object[] data = getUpdatedData(session, targets, baseTable, updateColumnMap, updateExpressions, colTypes, row.getData());
            try {
                updateRowSet.addRow(session, row, data, colTypes, updateColumnMap);
            } catch (HsqlException e) {
                for (int i = 0; i < joinRangeIterators.length; i++) {
                    rangeIterators[i].reset();
                }
                throw Error.error(ErrorCode.X_21000);
            }
        }
    }
    for (int i = 0; i < joinRangeIterators.length; i++) {
        rangeIterators[i].reset();
    }
    // run the transaction as a whole, updating and inserting where needed 
    // update any matched rows 
    if (updateExpressions.length != 0) {
        count = update(session, baseTable, updateRowSet);
    }
    // insert any non-matched rows 
    if (newData.getSize() > 0) {
        insertRowSet(session, generatedNavigator, newData);
        count += newData.getSize();
    }
    if (insertExpression != null && baseTable.triggerLists[Trigger.INSERT_AFTER].length > 0) {
        baseTable.fireTriggers(session, Trigger.INSERT_AFTER, newData);
    }
    if (resultOut == null) {
        if (count == 1) {
            return Result.updateOneResult;
        }
        return new Result(ResultConstants.UPDATECOUNT, count);
    } else {
        resultOut.setUpdateCount(count);
        return resultOut;
    }
}
