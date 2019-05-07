/**
     * needs to provide list of specific referenced routines
     */
Table ROUTINE_ROUTINE_USAGE(Session session) {
    Table t = sysTables[ROUTINE_ROUTINE_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROUTINE_ROUTINE_USAGE]);
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_NAME", SQL_IDENTIFIER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROUTINE_ROUTINE_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    // column number mappings 
    final int specific_catalog = 0;
    final int specific_schema = 1;
    final int specific_name = 2;
    final int routine_catalog = 3;
    final int routine_schema = 4;
    final int routine_name = 5;
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
                if (refName.type != SchemaObject.SPECIFIC_ROUTINE) {
                    continue;
                }
                if (!session.getGrantee().isAccessible(refName)) {
                    continue;
                }
                row = t.getEmptyRowData();
                row[specific_catalog] = database.getCatalogName().name;
                row[specific_schema] = specifics[m].getSchemaName().name;
                row[specific_name] = specifics[m].getSpecificName().name;
                row[routine_catalog] = database.getCatalogName().name;
                row[routine_schema] = refName.schema.name;
                row[routine_name] = refName.name;
                try {
                    t.insertSys(store, row);
                } catch (HsqlException e) {
                }
            }
        }
    }
    return t;
}
