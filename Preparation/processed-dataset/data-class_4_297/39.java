Table ROLE_ROUTINE_GRANTS(Session session) {
    Table t = sysTables[ROLE_ROUTINE_GRANTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROLE_ROUTINE_GRANTS]);
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
        addColumn(t, "PRIVILEGE_TYPE", CHARACTER_DATA);
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROLE_ROUTINE_GRANTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    Result rs = sys.executeDirectStatement("SELECT GRANTOR, GRANTEE, SPECIFIC_CATALOG, SPECIFIC_SCHEMA, " + "SPECIFIC_NAME, ROUTINE_CATALOG, ROUTINE_SCHEMA, ROUTINE_NAME, " + "PRIVILEGE_TYPE, IS_GRANTABLE " + "FROM INFORMATION_SCHEMA.ROUTINE_PRIVILEGES " + "JOIN INFORMATION_SCHEMA.APPLICABLE_ROLES ON GRANTEE = ROLE_NAME;", ResultProperties.defaultPropsValue);
    t.insertSys(store, rs);
    sys.close();
    // Column number mappings 
    final int grantor = 0;
    final int grantee = 1;
    final int table_name = 2;
    final int specific_catalog = 3;
    final int specific_schema = 4;
    final int specific_name = 5;
    final int routine_catalog = 6;
    final int routine_schema = 7;
    final int routine_name = 8;
    final int privilege_type = 9;
    final int is_grantable = 10;
    // 
    return t;
}
