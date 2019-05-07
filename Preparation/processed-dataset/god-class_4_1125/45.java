Table ROUTINE_JAR_USAGE(Session session) {
    Table t = sysTables[ROUTINE_JAR_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROUTINE_JAR_USAGE]);
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        addColumn(t, "JAR_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "JAR_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "JAR_NAME", SQL_IDENTIFIER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROUTINE_JAR_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    // column number mappings 
    final int specific_catalog = 0;
    final int specific_schema = 1;
    final int specific_name = 2;
    final int jar_catalog = 3;
    final int jar_schema = 4;
    final int jar_name = 5;
    // 
    Iterator it;
    Object[] row;
    PersistentStore store = session.sessionData.getRowStore(t);
    it = database.schemaManager.databaseObjectIterator(SchemaObject.ROUTINE);
    while (it.hasNext()) {
        RoutineSchema routine = (RoutineSchema) it.next();
        if (!session.getGrantee().isAccessible(routine)) {
            continue;
        }
        Routine[] specifics = routine.getSpecificRoutines();
        for (int m = 0; m < specifics.length; m++) {
            if (specifics[m].getLanguage() != Routine.LANGUAGE_JAVA) {
                continue;
            }
            row = t.getEmptyRowData();
            row[specific_catalog] = database.getCatalogName().name;
            row[specific_schema] = routine.getSchemaName().name;
            row[specific_name] = routine.getName().name;
            row[jar_catalog] = database.getCatalogName().name;
            row[jar_schema] = database.schemaManager.getSQLJSchemaHsqlName();
            row[jar_name] = "CLASSPATH";
            t.insertSys(store, row);
        }
    }
    return t;
}
