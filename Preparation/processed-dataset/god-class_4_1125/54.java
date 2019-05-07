Table SQL_PARTS(Session session) {
    Table t = sysTables[SQL_PARTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SQL_PARTS]);
        addColumn(t, "PART", CHARACTER_DATA);
        addColumn(t, "NAME", CHARACTER_DATA);
        addColumn(t, "IS_SUPPORTED", YES_OR_NO);
        addColumn(t, "IS_VERIFIED_BY", CHARACTER_DATA);
        addColumn(t, "COMMENTS", CHARACTER_DATA);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SQL_PARTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    String sql = (String) statementMap.get("/*sql_parts*/");
    Result rs = sys.executeDirectStatement(sql, ResultProperties.defaultPropsValue);
    t.insertSys(store, rs);
    return t;
}
