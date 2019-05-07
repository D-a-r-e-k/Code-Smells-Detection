/**
     * Retrieves a <code>Table</code> object describing the
     * primary key and unique constraint columns of each accessible table
     * defined within this database. <p>
     *
     * Each row is a PRIMARY KEY or UNIQUE column description with the following
     * columns: <p>
     *
     * <pre class="SqlCodeExample">
     * CONSTRAINT_CATALOG              VARCHAR NULL,
     * CONSTRAINT_SCHEMA               VARCHAR NULL,
     * CONSTRAINT_NAME                 VARCHAR NOT NULL,
     * TABLE_CATALOG                   VARCHAR   table catalog
     * TABLE_SCHEMA                    VARCHAR   table schema
     * TABLE_NAME                      VARCHAR   table name
     * COLUMN_NAME                     VARCHAR   column name
     * ORDINAL_POSITION                INT
     * POSITION_IN_UNIQUE_CONSTRAINT   INT
     * </pre> <p>
     *
     * @return a <code>Table</code> object describing the visible
     *        primary key and unique columns of each accessible table
     *        defined within this database.
     */
Table KEY_COLUMN_USAGE(Session session) {
    Table t = sysTables[KEY_COLUMN_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[KEY_COLUMN_USAGE]);
        addColumn(t, "CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "COLUMN_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "ORDINAL_POSITION", CARDINAL_NUMBER);
        // not null 
        addColumn(t, "POSITION_IN_UNIQUE_CONSTRAINT", CARDINAL_NUMBER);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[KEY_COLUMN_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 2, 1, 0, 6, 7 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    Iterator tables;
    Object[] row;
    // column number mappings 
    final int constraint_catalog = 0;
    final int constraint_schema = 1;
    final int constraint_name = 2;
    final int table_catalog = 3;
    final int table_schema = 4;
    final int table_name = 5;
    final int column_name = 6;
    final int ordinal_position = 7;
    final int position_in_unique_constraint = 8;
    // Initialization 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    while (tables.hasNext()) {
        Table table = (Table) tables.next();
        String tableCatalog = database.getCatalogName().name;
        String tableSchema = table.getSchemaName().name;
        String tableName = table.getName().name;
        /** @todo - requires access to the actual columns */
        if (table.isView() || !isAccessibleTable(session, table)) {
            continue;
        }
        Constraint[] constraints = table.getConstraints();
        for (int i = 0; i < constraints.length; i++) {
            Constraint constraint = constraints[i];
            if (constraint.getConstraintType() == SchemaObject.ConstraintTypes.PRIMARY_KEY || constraint.getConstraintType() == SchemaObject.ConstraintTypes.UNIQUE || constraint.getConstraintType() == SchemaObject.ConstraintTypes.FOREIGN_KEY) {
                String constraintName = constraint.getName().name;
                int[] cols = constraint.getMainColumns();
                int[] uniqueColMap = null;
                if (constraint.getConstraintType() == SchemaObject.ConstraintTypes.FOREIGN_KEY) {
                    Table uniqueConstTable = constraint.getMain();
                    Constraint uniqueConstraint = uniqueConstTable.getConstraint(constraint.getUniqueName().name);
                    int[] uniqueConstIndexes = uniqueConstraint.getMainColumns();
                    uniqueColMap = new int[cols.length];
                    for (int j = 0; j < cols.length; j++) {
                        uniqueColMap[j] = ArrayUtil.find(uniqueConstIndexes, cols[j]);
                    }
                    cols = constraint.getRefColumns();
                }
                for (int j = 0; j < cols.length; j++) {
                    row = t.getEmptyRowData();
                    row[constraint_catalog] = tableCatalog;
                    row[constraint_schema] = tableSchema;
                    row[constraint_name] = constraintName;
                    row[table_catalog] = tableCatalog;
                    row[table_schema] = tableSchema;
                    row[table_name] = tableName;
                    row[column_name] = table.getColumn(cols[j]).getName().name;
                    row[ordinal_position] = ValuePool.getLong(j + 1);
                    if (constraint.getConstraintType() == SchemaObject.ConstraintTypes.FOREIGN_KEY) {
                        row[position_in_unique_constraint] = ValuePool.getInt(uniqueColMap[j] + 1);
                    }
                    t.insertSys(store, row);
                }
            }
        }
    }
    return t;
}
