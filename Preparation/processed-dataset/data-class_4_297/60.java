Table TRIGGER_ROUTINE_USAGE(Session session) {
    Table t = sysTables[TRIGGER_ROUTINE_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[TRIGGER_ROUTINE_USAGE]);
        addColumn(t, "TRIGGER_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TRIGGER_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TRIGGER_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[TRIGGER_ROUTINE_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // column number mappings 
    final int trigger_catalog = 0;
    final int trigger_schema = 1;
    final int trigger_name = 2;
    final int specific_catalog = 3;
    final int specific_schema = 4;
    final int specific_name = 5;
    Iterator it;
    Object[] row;
    it = database.schemaManager.databaseObjectIterator(SchemaObject.TRIGGER);
    while (it.hasNext()) {
        TriggerDef trigger = (TriggerDef) it.next();
        if (!session.getGrantee().isAccessible(trigger)) {
            continue;
        }
        OrderedHashSet set = trigger.getReferences();
        for (int i = 0; i < set.size(); i++) {
            HsqlName refName = (HsqlName) set.get(i);
            if (refName.type != SchemaObject.SPECIFIC_ROUTINE) {
                continue;
            }
            if (!session.getGrantee().isAccessible(refName)) {
                continue;
            }
            row = t.getEmptyRowData();
            row[trigger_catalog] = database.getCatalogName().name;
            row[trigger_schema] = trigger.getSchemaName().name;
            row[trigger_name] = trigger.getName().name;
            row[specific_catalog] = database.getCatalogName().name;
            row[specific_schema] = refName.schema.name;
            row[specific_name] = refName.name;
            try {
                t.insertSys(store, row);
            } catch (HsqlException e) {
            }
        }
    }
    return t;
}
