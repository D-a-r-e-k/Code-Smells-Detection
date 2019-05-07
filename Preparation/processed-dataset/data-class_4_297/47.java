Table ROUTINE_SEQUENCE_USAGE(Session session) {
    Table t = sysTables[ROUTINE_SEQUENCE_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROUTINE_SEQUENCE_USAGE]);
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        addColumn(t, "SEQUENCE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SEQUENCE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SEQUENCE_NAME", SQL_IDENTIFIER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROUTINE_SEQUENCE_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    // column number mappings 
    final int specific_catalog = 0;
    final int specific_schema = 1;
    final int specific_name = 2;
    final int sequence_catalog = 3;
    final int sequence_schema = 4;
    final int sequence_name = 5;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    Iterator it;
    Object[] row;
    it = database.schemaManager.databaseObjectIterator(SchemaObject.ROUTINE);
    while (it.hasNext()) {
        RoutineSchema routine = (RoutineSchema) it.next();
        if (!session.getGrantee().isAccessible(routine)) {
            continue;
        }
        Routine[] specifics = routine.getSpecificRoutines();
        for (int m = 0; m < specifics.length; m++) {
            OrderedHashSet set = specifics[m].getReferences();
            for (int i = 0; i < set.size(); i++) {
                HsqlName refName = (HsqlName) set.get(i);
                if (refName.type != SchemaObject.SEQUENCE) {
                    continue;
                }
                if (!session.getGrantee().isAccessible(refName)) {
                    continue;
                }
                row = t.getEmptyRowData();
                row[specific_catalog] = database.getCatalogName().name;
                row[specific_schema] = specifics[m].getSchemaName().name;
                row[specific_name] = specifics[m].getSpecificName().name;
                row[sequence_catalog] = database.getCatalogName().name;
                row[sequence_schema] = refName.schema.name;
                row[sequence_name] = refName.name;
                try {
                    t.insertSys(store, row);
                } catch (HsqlException e) {
                }
            }
        }
    }
    return t;
}
