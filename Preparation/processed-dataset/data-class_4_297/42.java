Table ROLE_USAGE_GRANTS(Session session) {
    Table t = sysTables[ROLE_USAGE_GRANTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROLE_USAGE_GRANTS]);
        addColumn(t, "GRANTOR", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "OBJECT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "OBJECT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "OBJECT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "OBJECT_TYPE", CHARACTER_DATA);
        // not null 
        addColumn(t, "PRIVILEGE_TYPE", CHARACTER_DATA);
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROLE_USAGE_GRANTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    Result rs = sys.executeDirectStatement("SELECT GRANTOR, GRANTEE, OBJECT_CATALOG, OBJECT_SCHEMA, OBJECT_NAME, " + "OBJECT_TYPE, PRIVILEGE_TYPE, IS_GRANTABLE " + "FROM INFORMATION_SCHEMA.USAGE_PRIVILEGES " + "JOIN INFORMATION_SCHEMA.APPLICABLE_ROLES ON GRANTEE = ROLE_NAME;", ResultProperties.defaultPropsValue);
    t.insertSys(store, rs);
    sys.close();
    return t;
}
