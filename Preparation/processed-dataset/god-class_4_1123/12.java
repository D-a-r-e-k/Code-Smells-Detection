/**
     * Highest level multiple row update method.<p>
     *
     * Following clauses from SQL Standard section 11.8 are enforced 9) Let ISS
     * be the innermost SQL-statement being executed. 10) If evaluation of these
     * General Rules during the execution of ISS would cause an update of some
     * site to a value that is distinct from the value to which that site was
     * previously updated during the execution of ISS, then an exception
     * condition is raised: triggered data change violation. 11) If evaluation
     * of these General Rules during the execution of ISS would cause deletion
     * of a row containing a site that is identified for replacement in that
     * row, then an exception condition is raised: triggered data change
     * violation.
     *
     * @param session Session
     * @param table Table
     * @param updateList RowSetNavigatorDataChange
     * @return int
     */
int update(Session session, Table table, RowSetNavigatorDataChange navigator) {
    int rowCount = navigator.getSize();
    // set identity column where null and check columns 
    for (int i = 0; i < rowCount; i++) {
        navigator.next();
        Object[] data = navigator.getCurrentChangedData();
        /**
             * @todo 1.9.0 - make optional using database property -
             * this means the identity column can be set to null to force
             * creation of a new identity value
             */
        table.setIdentityColumn(session, data);
        table.setGeneratedColumns(session, data);
    }
    navigator.beforeFirst();
    if (table.fkMainConstraints.length > 0) {
        HashSet path = session.sessionContext.getConstraintPath();
        for (int i = 0; i < rowCount; i++) {
            Row row = navigator.getNextRow();
            Object[] data = navigator.getCurrentChangedData();
            performReferentialActions(session, table, navigator, row, data, this.updateColumnMap, path);
            path.clear();
        }
        navigator.beforeFirst();
    }
    for (int i = 0; i < navigator.getSize(); i++) {
        Row row = navigator.getNextRow();
        Object[] data = navigator.getCurrentChangedData();
        int[] changedColumns = navigator.getCurrentChangedColumns();
        Table currentTable = ((Table) row.getTable());
        if (currentTable.triggerLists[Trigger.UPDATE_BEFORE_ROW].length > 0) {
            currentTable.fireTriggers(session, Trigger.UPDATE_BEFORE_ROW, row.getData(), data, changedColumns);
            currentTable.enforceRowConstraints(session, data);
        }
    }
    if (table.isView) {
        return rowCount;
    }
    navigator.beforeFirst();
    for (int i = 0; i < navigator.getSize(); i++) {
        Row row = navigator.getNextRow();
        Table currentTable = ((Table) row.getTable());
        int[] changedColumns = navigator.getCurrentChangedColumns();
        session.addDeleteAction(currentTable, row, changedColumns);
    }
    navigator.beforeFirst();
    for (int i = 0; i < navigator.getSize(); i++) {
        Row row = navigator.getNextRow();
        Object[] data = navigator.getCurrentChangedData();
        Table currentTable = ((Table) row.getTable());
        int[] changedColumns = navigator.getCurrentChangedColumns();
        PersistentStore store = currentTable.getRowStore(session);
        if (data == null) {
            continue;
        }
        Row newRow = currentTable.insertSingleRow(session, store, data, changedColumns);
    }
    navigator.beforeFirst();
    OrderedHashSet extraUpdateTables = null;
    boolean hasAfterRowTriggers = table.triggerLists[Trigger.UPDATE_AFTER_ROW].length > 0;
    for (int i = 0; i < navigator.getSize(); i++) {
        Row row = navigator.getNextRow();
        Table currentTable = ((Table) row.getTable());
        Object[] changedData = navigator.getCurrentChangedData();
        int[] changedColumns = navigator.getCurrentChangedColumns();
        performIntegrityChecks(session, currentTable, row.getData(), changedData, changedColumns);
        if (currentTable != table) {
            if (extraUpdateTables == null) {
                extraUpdateTables = new OrderedHashSet();
            }
            extraUpdateTables.add(currentTable);
            if (currentTable.triggerLists[Trigger.UPDATE_AFTER_ROW].length > 0) {
                hasAfterRowTriggers = true;
            }
        }
    }
    navigator.beforeFirst();
    if (hasAfterRowTriggers) {
        for (int i = 0; i < navigator.getSize(); i++) {
            Row row = navigator.getNextRow();
            Object[] changedData = navigator.getCurrentChangedData();
            int[] changedColumns = navigator.getCurrentChangedColumns();
            Table currentTable = ((Table) row.getTable());
            currentTable.fireTriggers(session, Trigger.UPDATE_AFTER_ROW, row.getData(), changedData, changedColumns);
        }
        navigator.beforeFirst();
    }
    baseTable.fireTriggers(session, Trigger.UPDATE_AFTER, navigator);
    if (extraUpdateTables != null) {
        for (int i = 0; i < extraUpdateTables.size(); i++) {
            Table currentTable = (Table) extraUpdateTables.get(i);
            currentTable.fireTriggers(session, Trigger.UPDATE_AFTER, navigator);
        }
    }
    return rowCount;
}
