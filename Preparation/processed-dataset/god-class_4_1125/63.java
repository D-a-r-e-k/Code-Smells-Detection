Table TRIGGERS(Session session) {
    Table t = sysTables[TRIGGERS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[TRIGGERS]);
        addColumn(t, "TRIGGER_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TRIGGER_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TRIGGER_NAME", SQL_IDENTIFIER);
        addColumn(t, "EVENT_MANIPULATION", SQL_IDENTIFIER);
        addColumn(t, "EVENT_OBJECT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "EVENT_OBJECT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "EVENT_OBJECT_TABLE", SQL_IDENTIFIER);
        addColumn(t, "ACTION_ORDER", CARDINAL_NUMBER);
        addColumn(t, "ACTION_CONDITION", CHARACTER_DATA);
        addColumn(t, "ACTION_STATEMENT", CHARACTER_DATA);
        addColumn(t, "ACTION_ORIENTATION", CHARACTER_DATA);
        addColumn(t, "ACTION_TIMING", CHARACTER_DATA);
        addColumn(t, "ACTION_REFERENCE_OLD_TABLE", SQL_IDENTIFIER);
        addColumn(t, "ACTION_REFERENCE_NEW_TABLE", SQL_IDENTIFIER);
        addColumn(t, "ACTION_REFERENCE_OLD_ROW", SQL_IDENTIFIER);
        addColumn(t, "ACTION_REFERENCE_NEW_ROW", SQL_IDENTIFIER);
        addColumn(t, "CREATED", TIME_STAMP);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[TRIGGERS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // column number mappings 
    final int trigger_catalog = 0;
    final int trigger_schema = 1;
    final int trigger_name = 2;
    final int event_manipulation = 3;
    final int event_object_catalog = 4;
    final int event_object_schema = 5;
    final int event_object_table = 6;
    final int action_order = 7;
    final int action_condition = 8;
    final int action_statement = 9;
    final int action_orientation = 10;
    final int action_timing = 11;
    final int action_reference_old_table = 12;
    final int action_reference_new_table = 13;
    final int action_reference_old_row = 14;
    final int action_reference_new_row = 15;
    final int created = 16;
    Iterator it;
    Object[] row;
    it = database.schemaManager.databaseObjectIterator(SchemaObject.TRIGGER);
    while (it.hasNext()) {
        TriggerDef trigger = (TriggerDef) it.next();
        if (!session.getGrantee().isAccessible(trigger)) {
            continue;
        }
        row = t.getEmptyRowData();
        row[trigger_catalog] = database.getCatalogName().name;
        row[trigger_schema] = trigger.getSchemaName().name;
        row[trigger_name] = trigger.getName().name;
        row[event_manipulation] = trigger.getEventTypeString();
        row[event_object_catalog] = database.getCatalogName().name;
        row[event_object_schema] = trigger.getTable().getSchemaName().name;
        row[event_object_table] = trigger.getTable().getName().name;
        int order = trigger.getTable().getTriggerIndex(trigger.getName().name);
        row[action_order] = ValuePool.getLong(order);
        row[action_condition] = trigger.getConditionSQL();
        row[action_statement] = trigger.getProcedureSQL();
        row[action_orientation] = trigger.getActionOrientationString();
        row[action_timing] = trigger.getActionTimingString();
        row[action_reference_old_table] = trigger.getOldTransitionTableName();
        row[action_reference_new_table] = trigger.getNewTransitionTableName();
        row[action_reference_old_row] = trigger.getOldTransitionRowName();
        row[action_reference_new_row] = trigger.getNewTransitionRowName();
        row[created] = null;
        t.insertSys(store, row);
    }
    // Initialization 
    return t;
}
