/**
     * The VIEW_TABLE_USAGE table has one row for each table identified
     * by a &lt;table name&gt; simply contained in a &lt;table reference&gt;
     * that is contained in the &lt;query expression&gt; of a view. <p>
     *
     * <b>Definition</b><p>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE SYSTEM_VIEW_TABLE_USAGE (
     *      VIEW_CATALOG    VARCHAR NULL,
     *      VIEW_SCHEMA     VARCHAR NULL,
     *      VIEW_NAME       VARCHAR NULL,
     *      TABLE_CATALOG   VARCHAR NULL,
     *      TABLE_SCHEMA    VARCHAR NULL,
     *      TABLE_NAME      VARCHAR NULL,
     *      UNIQUE( VIEW_CATALOG, VIEW_SCHEMA, VIEW_NAME,
     *              TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME )
     * )
     * </pre>
     *
     * <b>Description:</b><p>
     *
     * <ol>
     * <li> The values of VIEW_CATALOG, VIEW_SCHEMA, and VIEW_NAME are the
     *      catalog name, unqualified schema name, and qualified identifier,
     *      respectively, of the view being described. <p>
     *
     * <li> The values of TABLE_CATALOG, TABLE_SCHEMA, and TABLE_NAME are the
     *      catalog name, unqualified schema name, and qualified identifier,
     *      respectively, of a table identified by a &lt;table name&gt;
     *      simply contained in a &lt;table reference&gt; that is contained in
     *      the &lt;query expression&gt; of the view being described.
     * </ol>
     *
     * @return Table
     */
Table VIEW_TABLE_USAGE(Session session) {
    Table t = sysTables[VIEW_TABLE_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[VIEW_TABLE_USAGE]);
        addColumn(t, "VIEW_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "VIEW_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "VIEW_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        // false PK, as VIEW_CATALOG, VIEW_SCHEMA, TABLE_CATALOG, and/or 
        // TABLE_SCHEMA may be NULL 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[VIEW_TABLE_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    // Column number mappings 
    final int view_catalog = 0;
    final int view_schema = 1;
    final int view_name = 2;
    final int table_catalog = 3;
    final int table_schema = 4;
    final int table_name = 5;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    Iterator tables;
    Table table;
    Object[] row;
    // Initialization 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    // Do it. 
    while (tables.hasNext()) {
        table = (Table) tables.next();
        if (table.isView() && session.getGrantee().isFullyAccessibleByRole(table.getName())) {
        } else {
            continue;
        }
        OrderedHashSet references = table.getReferences();
        for (int i = 0; i < references.size(); i++) {
            HsqlName refName = (HsqlName) references.get(i);
            if (!session.getGrantee().isFullyAccessibleByRole(refName)) {
                continue;
            }
            if (refName.type != SchemaObject.TABLE) {
                continue;
            }
            row = t.getEmptyRowData();
            row[view_catalog] = database.getCatalogName().name;
            row[view_schema] = table.getSchemaName().name;
            row[view_name] = table.getName().name;
            row[table_catalog] = database.getCatalogName().name;
            row[table_schema] = refName.schema.name;
            row[table_name] = refName.name;
            try {
                t.insertSys(store, row);
            } catch (HsqlException e) {
            }
        }
    }
    return t;
}
