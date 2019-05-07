Table ROLE_TABLE_GRANTS(Session session) {
    Table t = sysTables[ROLE_TABLE_GRANTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROLE_TABLE_GRANTS]);
        addColumn(t, "GRANTOR", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "PRIVILEGE_TYPE", CHARACTER_DATA);
        // not null 
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        // not null 
        addColumn(t, "WITH_HIERARCHY", YES_OR_NO);
        // order:  TABLE_SCHEM, TABLE_NAME, and PRIVILEGE, 
        // added for unique:  GRANTEE, GRANTOR, 
        // false PK, as TABLE_SCHEM and/or TABLE_CAT may be null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROLE_TABLE_GRANTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 3, 4, 5, 0, 1 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    Result rs = sys.executeDirectStatement("SELECT GRANTOR, GRANTEE, TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, " + "PRIVILEGE_TYPE, IS_GRANTABLE, 'NO' " + "FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES " + "JOIN INFORMATION_SCHEMA.APPLICABLE_ROLES ON GRANTEE = ROLE_NAME;", ResultProperties.defaultPropsValue);
    t.insertSys(store, rs);
    sys.close();
    return t;
}
