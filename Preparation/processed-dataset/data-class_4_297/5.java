/**
     * Retrieves a <code>Table</code> object describing attributes
     * for the calling session context.<p>
     *
     * The rows report the following {key,value} pairs:<p>
     *
     * <pre class="SqlCodeExample">
     * KEY (VARCHAR)       VALUE (VARCHAR)
     * ------------------- ---------------
     * SESSION_ID          the id of the calling session
     * AUTOCOMMIT          YES: session is in autocommit mode, else NO
     * USER                the name of user connected in the calling session
     * (was READ_ONLY)
     * SESSION_READONLY    TRUE: session is in read-only mode, else FALSE
     * (new)
     * DATABASE_READONLY   TRUE: database is in read-only mode, else FALSE
     * MAXROWS             the MAXROWS setting in the calling session
     * DATABASE            the name of the database
     * IDENTITY            the last identity value used by calling session
     * </pre>
     *
     * <b>Note:</b>  This table <em>may</em> become deprecated in a future
     * release, as the information it reports now duplicates information
     * reported in the newer SYSTEM_SESSIONS and SYSTEM_PROPERTIES
     * tables. <p>
     *
     * @return a <code>Table</code> object describing the
     *        attributes of the connection associated
     *        with the current execution context
     */
Table SYSTEM_SESSIONINFO(Session session) {
    Table t = sysTables[SYSTEM_SESSIONINFO];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SYSTEM_SESSIONINFO]);
        addColumn(t, "KEY", CHARACTER_DATA);
        // not null 
        addColumn(t, "VALUE", CHARACTER_DATA);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SYSTEM_SESSIONINFO].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0 }, true);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Object[] row;
    row = t.getEmptyRowData();
    row[0] = "SESSION ID";
    row[1] = String.valueOf(session.getId());
    t.insertSys(store, row);
    row = t.getEmptyRowData();
    row[0] = "AUTOCOMMIT";
    row[1] = session.isAutoCommit() ? Tokens.T_TRUE : Tokens.T_FALSE;
    t.insertSys(store, row);
    row = t.getEmptyRowData();
    row[0] = "USER";
    row[1] = session.getUsername();
    t.insertSys(store, row);
    row = t.getEmptyRowData();
    row[0] = "SESSION READONLY";
    row[1] = session.isReadOnlyDefault() ? Tokens.T_TRUE : Tokens.T_FALSE;
    t.insertSys(store, row);
    row = t.getEmptyRowData();
    row[0] = "DATABASE READONLY";
    row[1] = database.isReadOnly() ? Tokens.T_TRUE : Tokens.T_FALSE;
    t.insertSys(store, row);
    row = t.getEmptyRowData();
    row[0] = "DATABASE";
    row[1] = database.getURI();
    t.insertSys(store, row);
    row = t.getEmptyRowData();
    row[0] = "IDENTITY";
    row[1] = String.valueOf(session.getLastIdentity());
    t.insertSys(store, row);
    row = t.getEmptyRowData();
    row[0] = "CURRENT SCHEMA";
    row[1] = String.valueOf(session.getSchemaName(null));
    t.insertSys(store, row);
    return t;
}
