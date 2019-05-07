Table SQL_SIZING(Session session) {
    Table t = sysTables[SQL_SIZING];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SQL_SIZING]);
        addColumn(t, "SIZING_ID", CARDINAL_NUMBER);
        addColumn(t, "SIZING_NAME", CHARACTER_DATA);
        addColumn(t, "SUPPORTED_VALUE", CARDINAL_NUMBER);
        addColumn(t, "COMMENTS", CHARACTER_DATA);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SQL_SIZING].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    String sql = (String) statementMap.get("/*sql_sizing*/");
    Result rs = sys.executeDirectStatement(sql, ResultProperties.defaultPropsValue);
    t.insertSys(store, rs);
    return t;
}
