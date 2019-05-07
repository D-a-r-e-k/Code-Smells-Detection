Table ROLE_UDT_GRANTS(Session session) {
    Table t = sysTables[ROLE_UDT_GRANTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROLE_UDT_GRANTS]);
        addColumn(t, "GRANTOR", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "UDT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "UDT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "UDT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "PRIVILEGE_TYPE", CHARACTER_DATA);
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROLE_TABLE_GRANTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, null, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    Result rs = sys.executeDirectStatement("SELECT GRANTOR, GRANTEE, UDT_CATALOG, UDT_SCHEMA, UDT_NAME, " + "PRIVILEGE_TYPE, IS_GRANTABLE " + "FROM INFORMATION_SCHEMA.UDT_PRIVILEGES " + "JOIN INFORMATION_SCHEMA.APPLICABLE_ROLES ON GRANTEE = ROLE_NAME;", ResultProperties.defaultPropsValue);
    t.insertSys(store, rs);
    sys.close();
    return t;
}
