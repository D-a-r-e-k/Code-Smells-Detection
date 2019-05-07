Table SQL_IMPLEMENTATION_INFO(Session session) {
    Table t = sysTables[SQL_IMPLEMENTATION_INFO];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SQL_IMPLEMENTATION_INFO]);
        addColumn(t, "IMPLEMENTATION_INFO_ID", CHARACTER_DATA);
        addColumn(t, "IMPLEMENTATION_INFO_NAME", CHARACTER_DATA);
        addColumn(t, "INTEGER_VALUE", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_VALUE", CHARACTER_DATA);
        addColumn(t, "COMMENTS", CHARACTER_DATA);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SQL_IMPLEMENTATION_INFO].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    /*
        Result rs = sys.executeDirectStatement(
            "VALUES "
            + ";");

        t.insertSys(store, rs);
*/
    return t;
}
