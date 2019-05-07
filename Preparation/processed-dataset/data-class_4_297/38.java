Table ROLE_COLUMN_GRANTS(Session session) {
    Table t = sysTables[ROLE_COLUMN_GRANTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROLE_COLUMN_GRANTS]);
        addColumn(t, "GRANTOR", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "COLUMN_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "PRIVILEGE_TYPE", CHARACTER_DATA);
        // not null 
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        // not null 
        // order: COLUMN_NAME, PRIVILEGE 
        // for unique: GRANTEE, GRANTOR, TABLE_NAME, TABLE_SCHEMA, TABLE_CAT 
        // false PK, as TABLE_SCHEMA and/or TABLE_CAT may be null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROLE_COLUMN_GRANTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 5, 6, 1, 0, 4, 3, 2 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    Result rs = sys.executeDirectStatement("SELECT GRANTOR, GRANTEE, TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, PRIVILEGE_TYPE, IS_GRANTABLE " + "FROM INFORMATION_SCHEMA.COLUMN_PRIVILEGES " + "JOIN INFORMATION_SCHEMA.APPLICABLE_ROLES ON GRANTEE = ROLE_NAME;", ResultProperties.defaultPropsValue);
    t.insertSys(store, rs);
    sys.close();
    return t;
}
