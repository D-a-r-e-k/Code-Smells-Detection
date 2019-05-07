/**
     * The CONSTRAINT_COLUMN_USAGE view has one row for each column identified by
     * a table constraint or assertion.<p>
     *
     * <b>Definition:</b><p>
     *
     *      TABLE_CATALOG       VARCHAR
     *      TABLE_SCHEMA        VARCHAR
     *      TABLE_NAME          VARCHAR
     *      COLUMN_NAME         VARCHAR
     *      CONSTRAINT_CATALOG  VARCHAR
     *      CONSTRAINT_SCHEMA   VARCHAR
     *      CONSTRAINT_NAME     VARCHAR
     *
     * </pre>
     *
     * <b>Description:</b> <p>
     *
     * <ol>
     * <li> The values of TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, and
     *      COLUMN_NAME are the catalog name, unqualified schema name,
     *      qualified identifier, and column name, respectively, of a column
     *      identified by a &lt;column reference&gt; explicitly or implicitly
     *      contained in the &lt;search condition&gt; of the constraint
     *      being described.
     *
     * <li> The values of CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, and
     *      CONSTRAINT_NAME are the catalog name, unqualified schema name,
     *      and qualified identifier, respectively, of the constraint being
     *      described. <p>
     *
     * </ol>
     *
     * @return Table
     */
Table CONSTRAINT_COLUMN_USAGE(Session session) {
    Table t = sysTables[CONSTRAINT_COLUMN_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[CONSTRAINT_COLUMN_USAGE]);
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "COLUMN_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_NAME", SQL_IDENTIFIER);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[CONSTRAINT_COLUMN_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5, 6 }, false);
        return t;
    }
    // column number mappings 
    final int table_catalog = 0;
    final int table_schems = 1;
    final int table_name = 2;
    final int column_name = 3;
    final int constraint_catalog = 4;
    final int constraint_schema = 5;
    final int constraint_name = 6;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // calculated column values 
    String constraintCatalog;
    String constraintSchema;
    String constraintName;
    // Intermediate holders 
    Iterator tables;
    Table table;
    Constraint[] constraints;
    int constraintCount;
    Constraint constraint;
    Iterator iterator;
    Object[] row;
    // Initialization 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    // Do it. 
    while (tables.hasNext()) {
        table = (Table) tables.next();
        if (table.isView() || !session.getGrantee().isFullyAccessibleByRole(table.getName())) {
            continue;
        }
        constraints = table.getConstraints();
        constraintCount = constraints.length;
        constraintCatalog = database.getCatalogName().name;
        constraintSchema = table.getSchemaName().name;
        // process constraints 
        for (int i = 0; i < constraintCount; i++) {
            constraint = constraints[i];
            constraintName = constraint.getName().name;
            switch(constraint.getConstraintType()) {
                case SchemaObject.ConstraintTypes.CHECK:
                    {
                        OrderedHashSet expressions = constraint.getCheckColumnExpressions();
                        if (expressions == null) {
                            break;
                        }
                        iterator = expressions.iterator();
                        // calculate distinct column references 
                        while (iterator.hasNext()) {
                            ExpressionColumn expr = (ExpressionColumn) iterator.next();
                            HsqlName name = expr.getBaseColumnHsqlName();
                            if (name.type != SchemaObject.COLUMN) {
                                continue;
                            }
                            row = t.getEmptyRowData();
                            row[table_catalog] = database.getCatalogName().name;
                            row[table_schems] = name.schema.name;
                            row[table_name] = name.parent.name;
                            row[column_name] = name.name;
                            row[constraint_catalog] = constraintCatalog;
                            row[constraint_schema] = constraintSchema;
                            row[constraint_name] = constraintName;
                            try {
                                t.insertSys(store, row);
                            } catch (HsqlException e) {
                            }
                        }
                        break;
                    }
                case SchemaObject.ConstraintTypes.UNIQUE:
                case SchemaObject.ConstraintTypes.PRIMARY_KEY:
                case SchemaObject.ConstraintTypes.FOREIGN_KEY:
                    {
                        Table target = table;
                        int[] cols = constraint.getMainColumns();
                        if (constraint.getConstraintType() == SchemaObject.ConstraintTypes.FOREIGN_KEY) {
                            target = constraint.getMain();
                        }
                        /*
                       checkme - it seems foreign key columns are not included
                       but columns of the referenced unique constraint are included

                        if (constraint.getType() == Constraint.FOREIGN_KEY) {
                            for (int j = 0; j < cols.length; j++) {
                                row = t.getEmptyRowData();

                                Table mainTable = constraint.getMain();

                                row[table_catalog] = database.getCatalog();
                                row[table_schems] =
                                    mainTable.getSchemaName().name;
                                row[table_name] = mainTable.getName().name;
                                row[column_name] = mainTable.getColumn(
                                    cols[j]).columnName.name;
                                row[constraint_catalog] = constraintCatalog;
                                row[constraint_schema]  = constraintSchema;
                                row[constraint_name]    = constraintName;

                                try {
                                    t.insertSys(row);
                                } catch (HsqlException e) {}
                            }

                            cols = constraint.getRefColumns();
                        }
*/
                        for (int j = 0; j < cols.length; j++) {
                            row = t.getEmptyRowData();
                            row[table_catalog] = database.getCatalogName().name;
                            row[table_schems] = constraintSchema;
                            row[table_name] = target.getName().name;
                            row[column_name] = target.getColumn(cols[j]).getName().name;
                            row[constraint_catalog] = constraintCatalog;
                            row[constraint_schema] = constraintSchema;
                            row[constraint_name] = constraintName;
                            try {
                                t.insertSys(store, row);
                            } catch (HsqlException e) {
                            }
                        }
                    }
            }
        }
    }
    return t;
}
