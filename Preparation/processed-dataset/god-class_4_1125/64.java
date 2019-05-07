Table TRIGGERED_UPDATE_COLUMNS(Session session) {
    Table t = sysTables[TRIGGERED_UPDATE_COLUMNS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[TRIGGERED_UPDATE_COLUMNS]);
        addColumn(t, "TRIGGER_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TRIGGER_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TRIGGER_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "EVENT_OBJECT_CATALOG", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "EVENT_OBJECT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "EVENT_OBJECT_TABLE", SQL_IDENTIFIER);
        addColumn(t, "EVENT_OBJECT_COLUMN", SQL_IDENTIFIER);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[TRIGGERED_UPDATE_COLUMNS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5, 6 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // column number mappings 
    final int trigger_catalog = 0;
    final int trigger_schema = 1;
    final int trigger_name = 2;
    final int event_object_catalog = 3;
    final int event_object_schema = 4;
    final int event_object_table = 5;
    final int event_object_column = 6;
    Iterator it;
    Object[] row;
    it = database.schemaManager.databaseObjectIterator(SchemaObject.TRIGGER);
    while (it.hasNext()) {
        TriggerDef trigger = (TriggerDef) it.next();
        if (!session.getGrantee().isAccessible(trigger)) {
            continue;
        }
        int[] colIndexes = trigger.getUpdateColumnIndexes();
        if (colIndexes == null) {
            continue;
        }
        for (int i = 0; i < colIndexes.length; i++) {
            ColumnSchema column = trigger.getTable().getColumn(colIndexes[i]);
            row = t.getEmptyRowData();
            row[trigger_catalog] = database.getCatalogName().name;
            row[trigger_schema] = trigger.getSchemaName().name;
            row[trigger_name] = trigger.getName().name;
            row[event_object_catalog] = database.getCatalogName().name;
            row[event_object_schema] = trigger.getTable().getSchemaName().name;
            row[event_object_table] = trigger.getTable().getName().name;
            row[event_object_column] = column.getNameString();
            t.insertSys(store, row);
        }
    }
    // Initialization 
    return t;
}
