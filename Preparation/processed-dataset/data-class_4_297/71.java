/**
     * The VIEWS view contains one row for each VIEW definition. <p>
     *
     * Each row is a description of the query expression that defines its view,
     * with the following columns:
     *
     * <pre class="SqlCodeExample">
     * TABLE_CATALOG    VARCHAR     name of view's defining catalog.
     * TABLE_SCHEMA     VARCHAR     name of view's defining schema.
     * TABLE_NAME       VARCHAR     the simple name of the view.
     * VIEW_DEFINITION  VARCHAR     the character representation of the
     *                              &lt;query expression&gt; contained in the
     *                              corresponding &lt;view descriptor&gt;.
     * CHECK_OPTION     VARCHAR     {"CASCADED" | "LOCAL" | "NONE"}
     * IS_UPDATABLE     VARCHAR     {"YES" | "NO"}
     * INSERTABLE_INTO VARCHAR      {"YES" | "NO"}
     * IS_TRIGGER_UPDATABLE        VARCHAR  {"YES" | "NO"}
     * IS_TRIGGER_DELETEABLE       VARCHAR  {"YES" | "NO"}
     * IS_TRIGGER_INSERTABLE_INTO  VARCHAR  {"YES" | "NO"}
     * </pre> <p>
     *
     * @return a tabular description of the text source of all
     *        <code>View</code> objects accessible to
     *        the user.
     */
Table VIEWS(Session session) {
    Table t = sysTables[VIEWS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[VIEWS]);
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "VIEW_DEFINITION", CHARACTER_DATA);
        // not null 
        addColumn(t, "CHECK_OPTION", CHARACTER_DATA);
        // not null 
        addColumn(t, "IS_UPDATABLE", YES_OR_NO);
        // not null 
        addColumn(t, "INSERTABLE_INTO", YES_OR_NO);
        // not null 
        addColumn(t, "IS_TRIGGER_UPDATABLE", YES_OR_NO);
        // not null 
        addColumn(t, "IS_TRIGGER_DELETABLE", YES_OR_NO);
        // not null 
        addColumn(t, "IS_TRIGGER_INSERTABLE_INTO", YES_OR_NO);
        // not null 
        // order TABLE_NAME 
        // added for unique: TABLE_SCHEMA, TABLE_CATALOG 
        // false PK, as TABLE_SCHEMA and/or TABLE_CATALOG may be null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[VIEWS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 1, 2, 0 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    Iterator tables;
    Table table;
    Object[] row;
    final int table_catalog = 0;
    final int table_schema = 1;
    final int table_name = 2;
    final int view_definition = 3;
    final int check_option = 4;
    final int is_updatable = 5;
    final int insertable_into = 6;
    final int is_trigger_updatable = 7;
    final int is_trigger_deletable = 8;
    final int is_trigger_insertable_into = 9;
    tables = allTables();
    while (tables.hasNext()) {
        table = (Table) tables.next();
        if ((table.getSchemaName() != SqlInvariants.INFORMATION_SCHEMA_HSQLNAME && !table.isView()) || !isAccessibleTable(session, table)) {
            continue;
        }
        row = t.getEmptyRowData();
        row[table_catalog] = database.getCatalogName().name;
        row[table_schema] = table.getSchemaName().name;
        row[table_name] = table.getName().name;
        String check = Tokens.T_NONE;
        if (table instanceof View) {
            if (session.getGrantee().isFullyAccessibleByRole(table.getName())) {
                row[view_definition] = ((View) table).getStatement();
            }
            switch(((View) table).getCheckOption()) {
                case SchemaObject.ViewCheckModes.CHECK_NONE:
                    break;
                case SchemaObject.ViewCheckModes.CHECK_LOCAL:
                    check = Tokens.T_LOCAL;
                    break;
                case SchemaObject.ViewCheckModes.CHECK_CASCADE:
                    check = Tokens.T_CASCADED;
                    break;
            }
        }
        row[check_option] = check;
        row[is_updatable] = table.isUpdatable() ? Tokens.T_YES : Tokens.T_NO;
        row[insertable_into] = table.isInsertable() ? Tokens.T_YES : Tokens.T_NO;
        row[is_trigger_updatable] = null;
        // only applies to INSTEAD OF triggers 
        row[is_trigger_deletable] = null;
        row[is_trigger_insertable_into] = null;
        t.insertSys(store, row);
    }
    return t;
}
