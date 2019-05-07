/**
     * Retrieves a <code>Table</code> object describing all visible
     * sessions. ADMIN users see *all* sessions
     * while non-admin users see only their own session.<p>
     *
     * Each row is a session state description with the following columns: <p>
     *
     * <pre class="SqlCodeExample">
     * SESSION_ID         BIGINT    session identifier
     * CONNECTED          TIMESTAMP time at which session was created
     * USER_NAME          VARCHAR   db user name of current session user
     * IS_ADMIN           BOOLEAN   is session user an admin user?
     * AUTOCOMMIT         BOOLEAN   is session in autocommit mode?
     * READONLY           BOOLEAN   is session in read-only mode?
     * LAST_IDENTITY      BIGINT    last identity value used by this session
     * TRANSACTION_SIZE   BIGINT   # of undo items in current transaction
     * SCHEMA             VARCHAR   current schema for session
     * </pre> <p>
     *
     * @return a <code>Table</code> object describing all visible
     *      sessions
     */
Table SYSTEM_SESSIONS(Session session) {
    Table t = sysTables[SYSTEM_SESSIONS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SYSTEM_SESSIONS]);
        addColumn(t, "SESSION_ID", CARDINAL_NUMBER);
        addColumn(t, "CONNECTED", TIME_STAMP);
        addColumn(t, "USER_NAME", SQL_IDENTIFIER);
        addColumn(t, "IS_ADMIN", Type.SQL_BOOLEAN);
        addColumn(t, "AUTOCOMMIT", Type.SQL_BOOLEAN);
        addColumn(t, "READONLY", Type.SQL_BOOLEAN);
        // Note: some sessions may have a NULL LAST_IDENTITY value 
        addColumn(t, "LAST_IDENTITY", CARDINAL_NUMBER);
        addColumn(t, "TRANSACTION_SIZE", CARDINAL_NUMBER);
        addColumn(t, "SCHEMA", SQL_IDENTIFIER);
        // order:  SESSION_ID 
        // true primary key 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SYSTEM_SESSIONS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0 }, true);
        return t;
    }
    // column number mappings 
    final int isid = 0;
    final int ict = 1;
    final int iuname = 2;
    final int iis_admin = 3;
    final int iautocmt = 4;
    final int ireadonly = 5;
    final int ilast_id = 6;
    final int it_size = 7;
    final int it_schema = 8;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // intermediate holders 
    Session[] sessions;
    Session s;
    Object[] row;
    // Initialisation 
    sessions = database.sessionManager.getVisibleSessions(session);
    // Do it. 
    for (int i = 0; i < sessions.length; i++) {
        s = sessions[i];
        row = t.getEmptyRowData();
        row[isid] = ValuePool.getLong(s.getId());
        row[ict] = new TimestampData(s.getConnectTime() / 1000);
        row[iuname] = s.getUsername();
        row[iis_admin] = ValuePool.getBoolean(s.isAdmin());
        row[iautocmt] = ValuePool.getBoolean(s.isAutoCommit());
        row[ireadonly] = ValuePool.getBoolean(s.isReadOnlyDefault());
        row[ilast_id] = ValuePool.getLong(((Number) s.getLastIdentity()).longValue());
        row[it_size] = ValuePool.getLong(s.getTransactionSize());
        row[it_schema] = s.getCurrentSchemaHsqlName().name;
        t.insertSys(store, row);
    }
    return t;
}
