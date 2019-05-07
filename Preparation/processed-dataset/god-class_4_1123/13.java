/**
     * Executes a DELETE statement.
     *
     * @return the result of executing the statement
     */
Result executeDeleteStatement(Session session) {
    int count = 0;
    RangeIterator it = RangeVariable.getIterator(session, targetRangeVariables);
    RowSetNavigatorDataChange navigator = new RowSetNavigatorDataChange();
    while (it.next()) {
        Row currentRow = it.getCurrentRow();
        navigator.addRow(currentRow);
    }
    it.release();
    if (navigator.getSize() > 0) {
        count = delete(session, baseTable, navigator);
    } else {
        return Result.updateZeroResult;
    }
    if (count == 1) {
        return Result.updateOneResult;
    }
    return new Result(ResultConstants.UPDATECOUNT, count);
}
