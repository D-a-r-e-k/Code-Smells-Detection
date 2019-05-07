void insertRowSet(Session session, RowSetNavigator generatedNavigator, RowSetNavigator newData) {
    PersistentStore store = baseTable.getRowStore(session);
    RangeIterator checkIterator = null;
    if (updatableTableCheck != null) {
        checkIterator = checkRangeVariable.getIterator(session);
    }
    newData.beforeFirst();
    if (baseTable.triggerLists[Trigger.INSERT_BEFORE_ROW].length > 0) {
        while (newData.hasNext()) {
            Object[] data = (Object[]) newData.getNext();
            baseTable.fireTriggers(session, Trigger.INSERT_BEFORE_ROW, null, data, null);
        }
        newData.beforeFirst();
    }
    while (newData.hasNext()) {
        Object[] data = (Object[]) newData.getNext();
        baseTable.insertSingleRow(session, store, data, null);
        if (checkIterator != null) {
            checkIterator.setCurrent(data);
            boolean check = updatableTableCheck.testCondition(session);
            if (!check) {
                throw Error.error(ErrorCode.X_44000);
            }
        }
        if (generatedNavigator != null) {
            Object[] generatedValues = getGeneratedColumns(data);
            generatedNavigator.add(generatedValues);
        }
    }
    newData.beforeFirst();
    while (newData.hasNext()) {
        Object[] data = (Object[]) newData.getNext();
        performIntegrityChecks(session, baseTable, null, data, null);
    }
    newData.beforeFirst();
    if (baseTable.triggerLists[Trigger.INSERT_AFTER_ROW].length > 0) {
        while (newData.hasNext()) {
            Object[] data = (Object[]) newData.getNext();
            baseTable.fireTriggers(session, Trigger.INSERT_AFTER_ROW, null, data, null);
        }
        newData.beforeFirst();
    }
}
