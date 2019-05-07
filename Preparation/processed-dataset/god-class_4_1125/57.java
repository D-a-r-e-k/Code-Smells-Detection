/**
     * The TABLE_CONSTRAINTS table has one row for each table constraint
     * associated with a table.  <p>
     *
     * It effectively contains a representation of the table constraint
     * descriptors. <p>
     *
     * <b>Definition:</b> <p>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE SYSTEM_TABLE_CONSTRAINTS (
     *      CONSTRAINT_CATALOG      VARCHAR NULL,
     *      CONSTRAINT_SCHEMA       VARCHAR NULL,
     *      CONSTRAINT_NAME         VARCHAR NOT NULL,
     *      CONSTRAINT_TYPE         VARCHAR NOT NULL,
     *      TABLE_CATALOG           VARCHAR NULL,
     *      TABLE_SCHEMA            VARCHAR NULL,
     *      TABLE_NAME              VARCHAR NOT NULL,
     *      IS_DEFERRABLE           VARCHAR NOT NULL,
     *      INITIALLY_DEFERRED      VARCHAR NOT NULL,
     *
     *      CHECK ( CONSTRAINT_TYPE IN
     *                      ( 'UNIQUE', 'PRIMARY KEY',
     *                        'FOREIGN KEY', 'CHECK' ) ),
     *
     *      CHECK ( ( IS_DEFERRABLE, INITIALLY_DEFERRED ) IN
     *              ( VALUES ( 'NO',  'NO'  ),
     *                       ( 'YES', 'NO'  ),
     *                       ( 'YES', 'YES' ) ) )
     * )
     * </pre>
     *
     * <b>Description:</b> <p>
     *
     * <ol>
     * <li> The values of CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, and
     *      CONSTRAINT_NAME are the catalog name, unqualified schema
     *      name, and qualified identifier, respectively, of the
     *      constraint being described. If the &lt;table constraint
     *      definition&gt; or &lt;add table constraint definition&gt;
     *      that defined the constraint did not specify a
     *      &lt;constraint name&gt;, then the values of CONSTRAINT_CATALOG,
     *      CONSTRAINT_SCHEMA, and CONSTRAINT_NAME are
     *      implementation-defined. <p>
     *
     * <li> The values of CONSTRAINT_TYPE have the following meanings: <p>
     *  <table border cellpadding="3">
     *  <tr>
     *      <td nowrap>FOREIGN KEY</td>
     *      <td nowrap>The constraint being described is a
     *                 foreign key constraint.</td>
     *  </tr>
     *  <tr>
     *      <td nowrap>UNIQUE</td>
     *      <td nowrap>The constraint being described is a
     *                 unique constraint.</td>
     *  </tr>
     *  <tr>
     *      <td nowrap>PRIMARY KEY</td>
     *      <td nowrap>The constraint being described is a
     *                 primary key constraint.</td>
     *  </tr>
     *  <tr>
     *      <td nowrap>CHECK</td>
     *      <td nowrap>The constraint being described is a
     *                 check constraint.</td>
     *  </tr>
     * </table> <p>
     *
     * <li> The values of TABLE_CATALOG, TABLE_SCHEMA, and TABLE_NAME are
     *      the catalog name, the unqualified schema name, and the
     *      qualified identifier of the name of the table to which the
     *      table constraint being described applies. <p>
     *
     * <li> The values of IS_DEFERRABLE have the following meanings: <p>
     *
     *  <table>
     *      <tr>
     *          <td nowrap>YES</td>
     *          <td nowrap>The table constraint is deferrable.</td>
     *      </tr>
     *      <tr>
     *          <td nowrap>NO</td>
     *          <td nowrap>The table constraint is not deferrable.</td>
     *      </tr>
     *  </table> <p>
     *
     * <li> The values of INITIALLY_DEFERRED have the following meanings: <p>
     *
     *  <table>
     *      <tr>
     *          <td nowrap>YES</td>
     *          <td nowrap>The table constraint is initially deferred.</td>
     *      </tr>
     *      <tr>
     *          <td nowrap>NO</td>
     *          <td nowrap>The table constraint is initially immediate.</td>
     *      </tr>
     *  </table> <p>
     * </ol>
     *
     * @return Table
     */
Table TABLE_CONSTRAINTS(Session session) {
    Table t = sysTables[TABLE_CONSTRAINTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[TABLE_CONSTRAINTS]);
        addColumn(t, "CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "CONSTRAINT_TYPE", CHARACTER_DATA);
        // not null 
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "IS_DEFERRABLE", YES_OR_NO);
        // not null 
        addColumn(t, "INITIALLY_DEFERRED", YES_OR_NO);
        // not null 
        // false PK, as CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, 
        // TABLE_CATALOG and/or TABLE_SCHEMA may be null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[TABLE_CONSTRAINTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 4, 5, 6 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    Iterator tables;
    Table table;
    Constraint[] constraints;
    int constraintCount;
    Constraint constraint;
    String cat;
    String schem;
    Object[] row;
    // column number mappings 
    final int constraint_catalog = 0;
    final int constraint_schema = 1;
    final int constraint_name = 2;
    final int constraint_type = 3;
    final int table_catalog = 4;
    final int table_schema = 5;
    final int table_name = 6;
    final int is_deferable = 7;
    final int initially_deferred = 8;
    // initialization 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    table = null;
    // else compiler complains 
    // do it 
    while (tables.hasNext()) {
        table = (Table) tables.next();
        /** @todo - requires table level INSERT or UPDATE or DELETE or REFERENCES (not SELECT) right */
        if (table.isView() || !isAccessibleTable(session, table)) {
            continue;
        }
        constraints = table.getConstraints();
        constraintCount = constraints.length;
        for (int i = 0; i < constraintCount; i++) {
            constraint = constraints[i];
            row = t.getEmptyRowData();
            switch(constraint.getConstraintType()) {
                case SchemaObject.ConstraintTypes.CHECK:
                    {
                        row[constraint_type] = "CHECK";
                        break;
                    }
                case SchemaObject.ConstraintTypes.UNIQUE:
                    {
                        row[constraint_type] = "UNIQUE";
                        break;
                    }
                case SchemaObject.ConstraintTypes.FOREIGN_KEY:
                    {
                        row[constraint_type] = "FOREIGN KEY";
                        table = constraint.getRef();
                        break;
                    }
                case SchemaObject.ConstraintTypes.PRIMARY_KEY:
                    {
                        row[constraint_type] = "PRIMARY KEY";
                        break;
                    }
                case SchemaObject.ConstraintTypes.MAIN:
                default:
                    {
                        continue;
                    }
            }
            cat = database.getCatalogName().name;
            schem = table.getSchemaName().name;
            row[constraint_catalog] = cat;
            row[constraint_schema] = schem;
            row[constraint_name] = constraint.getName().name;
            row[table_catalog] = cat;
            row[table_schema] = schem;
            row[table_name] = table.getName().name;
            row[is_deferable] = Tokens.T_NO;
            row[initially_deferred] = Tokens.T_NO;
            t.insertSys(store, row);
        }
    }
    return t;
}
