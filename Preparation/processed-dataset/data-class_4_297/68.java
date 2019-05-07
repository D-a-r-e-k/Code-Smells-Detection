/**
     * The VIEW_COLUMN_USAGE table has one row for each column of a
     * table that is explicitly or implicitly referenced in the
     * &lt;query expression&gt; of the view being described. <p>
     *
     * <b>Definition:</b> <p>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE SYSTEM_VIEW_COLUMN_USAGE (
     *      VIEW_CATALOG    VARCHAR NULL,
     *      VIEW_SCHEMA     VARCHAR NULL,
     *      VIEW_NAME       VARCHAR NOT NULL,
     *      TABLE_CATALOG   VARCHAR NULL,
     *      TABLE_SCHEMA    VARCHAR NULL,
     *      TABLE_NAME      VARCHAR NOT NULL,
     *      COLUMN_NAME     VARCHAR NOT NULL,
     *      UNIQUE ( VIEW_CATALOG, VIEW_SCHEMA, VIEW_NAME,
     *               TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME,
     *               COLUMN_NAME )
     * )
     * </pre>
     *
     * <b>Description:</b> <p>
     *
     * <ol>
     * <li> The values of VIEW_CATALOG, VIEW_SCHEMA, and VIEW_NAME are the
     *      catalog name, unqualified schema name, and qualified identifier,
     *      respectively, of the view being described. <p>
     *
     * <li> The values of TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, and
     *      COLUMN_NAME are the catalog name, unqualified schema name,
     *      qualified identifier, and column name, respectively, of a column
     *      of a table that is explicitly or implicitly referenced in the
     *      &lt;query expression&gt; of the view being described.
     * </ol>
     *
     * @return Table
     */
Table VIEW_COLUMN_USAGE(Session session) {
    Table t = sysTables[VIEW_COLUMN_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[VIEW_COLUMN_USAGE]);
        addColumn(t, "VIEW_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "VIEW_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "VIEW_NAME", SQL_IDENTIFIER);
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        addColumn(t, "COLUMN_NAME", SQL_IDENTIFIER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[VIEW_COLUMN_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5, 6 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // Calculated column values 
    String viewCatalog;
    String viewSchema;
    String viewName;
    // Intermediate holders 
    Iterator tables;
    View view;
    Table table;
    Object[] row;
    Iterator iterator;
    // Column number mappings 
    final int view_catalog = 0;
    final int view_schema = 1;
    final int view_name = 2;
    final int table_catalog = 3;
    final int table_schema = 4;
    final int table_name = 5;
    final int column_name = 6;
    // Initialization 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    // Do it. 
    while (tables.hasNext()) {
        table = (Table) tables.next();
        if (table.isView() && session.getGrantee().isFullyAccessibleByRole(table.getName())) {
        } else {
            continue;
        }
        viewCatalog = database.getCatalogName().name;
        viewSchema = table.getSchemaName().name;
        viewName = table.getName().name;
        view = (View) table;
        OrderedHashSet references = view.getReferences();
        iterator = references.iterator();
        while (iterator.hasNext()) {
            HsqlName refName = (HsqlName) iterator.next();
            if (refName.type != SchemaObject.COLUMN) {
                continue;
            }
            row = t.getEmptyRowData();
            row[view_catalog] = viewCatalog;
            row[view_schema] = viewSchema;
            row[view_name] = viewName;
            row[table_catalog] = viewCatalog;
            row[table_schema] = refName.parent.schema.name;
            row[table_name] = refName.parent.name;
            row[column_name] = refName.name;
            try {
                t.insertSys(store, row);
            } catch (HsqlException e) {
            }
        }
    }
    return t;
}
