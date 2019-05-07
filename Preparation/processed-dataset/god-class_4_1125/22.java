/**
     * The CONSTRAINT_TABLE_USAGE view has one row for each table identified by a
     * &lt;table name&gt; simply contained in a &lt;table reference&gt;
     * contained in the &lt;search condition&gt; of a check constraint,
     * domain constraint, or assertion. It has one row for each table
     * containing / referenced by each PRIMARY KEY, UNIQUE and FOREIGN KEY
     * constraint<p>
     *
     * <b>Definition:</b> <p>
     *
     * <pre class="SqlCodeExample">
     *      CONSTRAINT_CATALOG      VARCHAR
     *      CONSTRAINT_SCHEMA       VARCHAR
     *      CONSTRAINT_NAME         VARCHAR
     *      TABLE_CATALOG           VARCHAR
     *      TABLE_SCHEMA            VARCHAR
     *      TABLE_NAME              VARCHAR
     * </pre>
     *
     * <b>Description:</b> <p>
     *
     * <ol>
     * <li> The values of CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, and
     *      CONSTRAINT_NAME are the catalog name, unqualified schema name,
     *       and qualified identifier, respectively, of the constraint being
     *      described. <p>
     *
     * <li> The values of TABLE_CATALOG, TABLE_SCHEMA, and TABLE_NAME are the
     *      catalog name, unqualified schema name, and qualified identifier,
     *      respectively, of a table identified by a &lt;table name&gt;
     *      simply contained in a &lt;table reference&gt; contained in the
     *      *lt;search condition&gt; of the constraint being described, or
     *      its columns.
     * </ol>
     *
     * @return Table
     */
Table CONSTRAINT_TABLE_USAGE(Session session) {
    Table t = sysTables[CONSTRAINT_TABLE_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[CONSTRAINT_TABLE_USAGE]);
        addColumn(t, "CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[CONSTRAINT_TABLE_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // 
    Session sys = database.sessionManager.newSysSession(SqlInvariants.INFORMATION_SCHEMA_HSQLNAME, session.getUser());
    Result rs = sys.executeDirectStatement("select DISTINCT CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, " + "CONSTRAINT_NAME, TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME " + "from INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE", ResultProperties.defaultPropsValue);
    t.insertSys(store, rs);
    sys.close();
    return t;
}
