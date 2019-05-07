/**
     *  Highest level multiple row delete method. Corresponds to an SQL
     *  DELETE.
     */
int delete(Session session, Table table, RowSetNavigatorDataChange navigator) {
    int rowCount = navigator.getSize();
    navigator.beforeFirst();
    if (table.fkMainConstraints.length > 0) {
        HashSet path = session.sessionContext.getConstraintPath();
        for (int i = 0; i < rowCount; i++) {
            navigator.next();
            Row row = navigator.getCurrentRow();
            performReferentialActions(session, table, navigator, row, null, null, path);
            path.clear();
        }
        navigator.beforeFirst();
    }
    while (navigator.hasNext()) {
        navigator.next();
        Row row = navigator.getCurrentRow();
        Object[] changedData = navigator.getCurrentChangedData();
        int[] changedColumns = navigator.getCurrentChangedColumns();
        Table currentTable = ((Table) row.getTable());
        if (changedData == null) {
            currentTable.fireTriggers(session, Trigger.DELETE_BEFORE_ROW, row.getData(), null, null);
        } else {
            currentTable.fireTriggers(session, Trigger.UPDATE_BEFORE_ROW, row.getData(), changedData, changedColumns);
        }
    }
    if (table.isView) {
        return rowCount;
    }
    navigator.beforeFirst();
    boolean hasUpdate = false;
    for (int i = 0; i < navigator.getSize(); i++) {
        Row row = navigator.getNextRow();
        Object[] data = navigator.getCurrentChangedData();
        Table currentTable = ((Table) row.getTable());
        session.addDeleteAction(currentTable, row, null);
        if (data != null) {
            hasUpdate = true;
        }
    }
    navigator.beforeFirst();
    if (hasUpdate) {
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
    }
    OrderedHashSet extraUpdateTables = null;
    OrderedHashSet extraDeleteTables = null;
    boolean hasAfterRowTriggers = table.triggerLists[Trigger.DELETE_AFTER_ROW].length > 0;
    if (rowCount != navigator.getSize()) {
        while (navigator.hasNext()) {
            navigator.next();
            Row row = navigator.getCurrentRow();
            Object[] changedData = navigator.getCurrentChangedData();
            int[] changedColumns = navigator.getCurrentChangedColumns();
            Table currentTable = ((Table) row.getTable());
            if (changedData != null) {
                performIntegrityChecks(session, currentTable, row.getData(), changedData, changedColumns);
            }
            if (currentTable != table) {
                if (changedData == null) {
                    if (currentTable.triggerLists[Trigger.DELETE_AFTER_ROW].length > 0) {
                        hasAfterRowTriggers = true;
                    }
                    if (extraDeleteTables == null) {
                        extraDeleteTables = new OrderedHashSet();
                    }
                    extraDeleteTables.add(currentTable);
                } else {
                    if (currentTable.triggerLists[Trigger.UPDATE_AFTER_ROW].length > 0) {
                        hasAfterRowTriggers = true;
                    }
                    if (extraUpdateTables == null) {
                        extraUpdateTables = new OrderedHashSet();
                    }
                    extraUpdateTables.add(currentTable);
                }
            }
        }
        navigator.beforeFirst();
    }
    if (hasAfterRowTriggers) {
        while (navigator.hasNext()) {
            navigator.next();
            Row row = navigator.getCurrentRow();
            Object[] changedData = navigator.getCurrentChangedData();
            Table currentTable = ((Table) row.getTable());
            if (changedData == null) {
                currentTable.fireTriggers(session, Trigger.DELETE_AFTER_ROW, row.getData(), null, null);
            } else {
                currentTable.fireTriggers(session, Trigger.UPDATE_AFTER_ROW, row.getData(), changedData, null);
            }
        }
        navigator.beforeFirst();
    }
    table.fireTriggers(session, Trigger.DELETE_AFTER, navigator);
    if (extraUpdateTables != null) {
        for (int i = 0; i < extraUpdateTables.size(); i++) {
            Table currentTable = (Table) extraUpdateTables.get(i);
            currentTable.fireTriggers(session, Trigger.UPDATE_AFTER, navigator);
        }
    }
    if (extraDeleteTables != null) {
        for (int i = 0; i < extraDeleteTables.size(); i++) {
            Table currentTable = (Table) extraDeleteTables.get(i);
            currentTable.fireTriggers(session, Trigger.DELETE_AFTER, navigator);
        }
    }
    return rowCount;
}
