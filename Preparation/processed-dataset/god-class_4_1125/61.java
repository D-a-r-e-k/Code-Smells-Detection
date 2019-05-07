Table TRIGGER_SEQUENCE_USAGE(Session session) {
    Table t = sysTables[TRIGGER_SEQUENCE_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[TRIGGER_SEQUENCE_USAGE]);
        addColumn(t, "TRIGGER_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TRIGGER_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TRIGGER_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "SEQUENCE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SEQUENCE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SEQUENCE_NAME", SQL_IDENTIFIER);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[TRIGGER_SEQUENCE_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // column number mappings 
    final int trigger_catalog = 0;
    final int trigger_schema = 1;
    final int trigger_name = 2;
    final int sequence_catalog = 3;
    final int sequence_schema = 4;
    final int sequence_name = 5;
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
            if (refName.type != SchemaObject.SEQUENCE) {
                continue;
            }
            if (!session.getGrantee().isAccessible(refName)) {
                continue;
            }
            row = t.getEmptyRowData();
            row[trigger_catalog] = database.getCatalogName().name;
            row[trigger_schema] = trigger.getSchemaName().name;
            row[trigger_name] = trigger.getName().name;
            row[sequence_catalog] = database.getCatalogName().name;
            row[sequence_schema] = refName.schema.name;
            row[sequence_name] = refName.name;
            try {
                t.insertSys(store, row);
            } catch (HsqlException e) {
            }
        }
    }
    // Initialization 
    return t;
}
