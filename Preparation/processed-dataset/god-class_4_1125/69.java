/**
     * The VIEW_ROUTINE_USAGE table has one row for each SQL-invoked
     * routine identified as the subject routine of either a &lt;routine
     * invocation&gt;, a &lt;method reference&gt;, a &lt;method invocation&gt;,
     * or a &lt;static method invocation&gt; contained in a &lt;view
     * definition&gt;. <p>
     *
     * <b>Definition</b><p>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE VIEW_ROUTINE_USAGE (
     *      TABLE_CATALOG       VARCHAR NULL,
     *      TABLE_SCHEMA        VARCHAR NULL,
     *      TABLE_NAME          VARCHAR NOT NULL,
     *      SPECIFIC_CATALOG    VARCHAR NULL,
     *      SPECIFIC_SCHEMA     VARCHAR NULL,
     *      SPECIFIC_NAME       VARCHAR NOT NULL,
     *      UNIQUE( TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME,
     *              SPECIFIC_CATALOG, SPECIFIC_SCHEMA,
     *              SPECIFIC_NAME )
     * )
     * </pre>
     *
     * <b>Description</b><p>
     *
     * <ol>
     * <li> The values of TABLE_CATALOG, TABLE_SCHEMA, and TABLE_NAME are the
     *      catalog name, unqualified schema name, and qualified identifier,
     *      respectively, of the viewed table being described. <p>
     *
     * <li> The values of SPECIFIC_CATALOG, SPECIFIC_SCHEMA, and SPECIFIC_NAME are
     *      the catalog name, unqualified schema name, and qualified identifier,
     *      respectively, of the specific name of R. <p>
     * </ol>
     *
     * @return Table
     */
Table VIEW_ROUTINE_USAGE(Session session) {
    Table t = sysTables[VIEW_ROUTINE_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[VIEW_ROUTINE_USAGE]);
        addColumn(t, "VIEW_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "VIEW_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "VIEW_NAME", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[VIEW_ROUTINE_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    Iterator tables;
    Table table;
    Object[] row;
    // Column number mappings 
    final int view_catalog = 0;
    final int view_schema = 1;
    final int view_name = 2;
    final int specific_catalog = 3;
    final int specific_schema = 4;
    final int specific_name = 5;
    // Initialization 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    // Do it. 
    while (tables.hasNext()) {
        table = (Table) tables.next();
        if (table.isView() && session.getGrantee().isFullyAccessibleByRole(table.getName())) {
        } else {
            continue;
        }
        OrderedHashSet set = table.getReferences();
        for (int i = 0; i < set.size(); i++) {
            HsqlName refName = (HsqlName) set.get(i);
            if (!session.getGrantee().isFullyAccessibleByRole(refName)) {
                continue;
            }
            if (refName.type != SchemaObject.SPECIFIC_ROUTINE) {
                continue;
            }
            row = t.getEmptyRowData();
            row[view_catalog] = database.getCatalogName().name;
            row[view_schema] = table.getSchemaName().name;
            row[view_name] = table.getName().name;
            row[specific_catalog] = database.getCatalogName().name;
            row[specific_schema] = refName.schema.name;
            row[specific_name] = refName.name;
            try {
                t.insertSys(store, row);
            } catch (HsqlException e) {
            }
        }
    }
    return t;
}
