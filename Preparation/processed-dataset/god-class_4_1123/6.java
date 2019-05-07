/**
     * Executes an UPDATE statement.
     *
     * @return Result object
     */
Result executeUpdateStatement(Session session) {
    int count = 0;
    Expression[] colExpressions = updateExpressions;
    RowSetNavigatorDataChange rowset = new RowSetNavigatorDataChange();
    Type[] colTypes = baseTable.getColumnTypes();
    RangeIterator it = RangeVariable.getIterator(session, targetRangeVariables);
    while (it.next()) {
        session.sessionData.startRowProcessing();
        Row row = it.getCurrentRow();
        Object[] data = row.getData();
        Object[] newData = getUpdatedData(session, targets, baseTable, updateColumnMap, colExpressions, colTypes, data);
        if (updatableTableCheck != null) {
            it.setCurrent(newData);
            boolean check = updatableTableCheck.testCondition(session);
            if (!check) {
                it.release();
                throw Error.error(ErrorCode.X_44000);
            }
        }
        rowset.addRow(session, row, newData, colTypes, updateColumnMap);
    }
    it.release();
    /* debug 190
        if (rowset.size() == 0) {
            System.out.println(targetTable.getName().name + " zero update: session "
                               + session.getId());
        } else if (rowset.size() >1) {
           System.out.println("multiple update: session "
                              + session.getId() + ", " + rowset.size());
       }

//* debug 190 */
    rowset.beforeFirst();
    count = update(session, baseTable, rowset);
    if (count == 1) {
        return Result.updateOneResult;
    } else if (count == 0) {
        return Result.updateZeroResult;
    }
    return new Result(ResultConstants.UPDATECOUNT, count);
}
