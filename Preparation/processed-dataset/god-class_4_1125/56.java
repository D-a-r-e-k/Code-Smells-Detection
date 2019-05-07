Table SQL_SIZING_PROFILES(Session session) {
    Table t = sysTables[SQL_SIZING_PROFILES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SQL_SIZING_PROFILES]);
        addColumn(t, "SIZING_ID", CARDINAL_NUMBER);
        addColumn(t, "SIZING_NAME", CHARACTER_DATA);
        addColumn(t, "PROFILE_ID", CARDINAL_NUMBER);
        addColumn(t, "PROFILE_NAME", CHARACTER_DATA);
        addColumn(t, "REQUIRED_VALUE", CARDINAL_NUMBER);
        addColumn(t, "COMMENTS", CHARACTER_DATA);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SQL_SIZING_PROFILES].name, false, SchemaObject.INDEX);
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
