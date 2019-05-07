Result insertSingleRow(Session session, PersistentStore store, Object[] data) {
    if (baseTable.triggerLists[Trigger.INSERT_BEFORE_ROW].length > 0) {
        baseTable.fireTriggers(session, Trigger.INSERT_BEFORE_ROW, null, data, null);
    }
    baseTable.insertSingleRow(session, store, data, null);
    performIntegrityChecks(session, baseTable, null, data, null);
    if (session.database.isReferentialIntegrity()) {
        for (int i = 0, size = baseTable.fkConstraints.length; i < size; i++) {
            baseTable.fkConstraints[i].checkInsert(session, baseTable, data, true);
        }
    }
    if (baseTable.triggerLists[Trigger.INSERT_AFTER_ROW].length > 0) {
        baseTable.fireTriggers(session, Trigger.INSERT_AFTER_ROW, null, data, null);
    }
    if (baseTable.triggerLists[Trigger.INSERT_AFTER].length > 0) {
        baseTable.fireTriggers(session, Trigger.INSERT_AFTER, (RowSetNavigator) null);
    }
    return Result.updateOneResult;
}
