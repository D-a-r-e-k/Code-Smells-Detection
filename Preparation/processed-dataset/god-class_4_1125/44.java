Table ROUTINE_PRIVILEGES(Session session) {
    Table t = sysTables[ROUTINE_PRIVILEGES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROUTINE_PRIVILEGES]);
        addColumn(t, "GRANTOR", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "ROUTINE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "PRIVILEGE_TYPE", CHARACTER_DATA);
        // not null 
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        // not null 
        // 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROUTINE_PRIVILEGES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, false);
        return t;
    }
    // column number mappings 
    final int grantor = 0;
    final int grantee = 1;
    final int specific_catalog = 2;
    final int specific_schema = 3;
    final int specific_name = 4;
    final int routine_catalog = 5;
    final int routine_schema = 6;
    final int routine_name = 7;
    final int privilege_type = 8;
    final int is_grantable = 9;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // calculated column values 
    Grantee granteeObject;
    String privilege;
    // intermediate holders 
    Iterator routines;
    RoutineSchema routine;
    Object[] row;
    OrderedHashSet grantees = session.getGrantee().getGranteeAndAllRolesWithPublic();
    routines = database.schemaManager.databaseObjectIterator(SchemaObject.ROUTINE);
    while (routines.hasNext()) {
        routine = (RoutineSchema) routines.next();
        for (int i = 0; i < grantees.size(); i++) {
            granteeObject = (Grantee) grantees.get(i);
            OrderedHashSet rights = granteeObject.getAllDirectPrivileges(routine);
            OrderedHashSet grants = granteeObject.getAllGrantedPrivileges(routine);
            if (!grants.isEmpty()) {
                grants.addAll(rights);
                rights = grants;
            }
            for (int j = 0; j < rights.size(); j++) {
                Right right = (Right) rights.get(j);
                Right grantableRight = right.getGrantableRights();
                for (int k = 0; k < Right.privilegeTypes.length; k++) {
                    if (!right.canAccessFully(Right.privilegeTypes[k])) {
                        continue;
                    }
                    Routine[] specifics = routine.getSpecificRoutines();
                    for (int m = 0; m < specifics.length; m++) {
                        privilege = Right.privilegeNames[k];
                        row = t.getEmptyRowData();
                        // 
                        row[grantor] = right.getGrantor().getName().name;
                        row[grantee] = right.getGrantee().getName().name;
                        row[specific_catalog] = database.getCatalogName().name;
                        row[specific_schema] = specifics[m].getSchemaName().name;
                        row[specific_name] = specifics[m].getSpecificName().name;
                        row[routine_catalog] = database.getCatalogName().name;
                        row[routine_schema] = routine.getSchemaName().name;
                        row[routine_name] = routine.getName().name;
                        row[privilege_type] = privilege;
                        row[is_grantable] = right.getGrantee() == routine.getOwner() || grantableRight.canAccessFully(Right.privilegeTypes[k]) ? "YES" : "NO";
                        try {
                            t.insertSys(store, row);
                        } catch (HsqlException e) {
                        }
                    }
                }
            }
        }
    }
    return t;
}
