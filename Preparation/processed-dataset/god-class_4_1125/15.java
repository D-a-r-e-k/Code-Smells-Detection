/**
     * The CHECK_CONSTRAINTS view has one row for each domain
     * constraint, table check constraint, and assertion. <p>
     *
     * <b>Definition:</b><p>
     *
     * <pre class="SqlCodeExample">
     *      CONSTRAINT_CATALOG  VARCHAR NULL,
     *      CONSTRAINT_SCHEMA   VARCHAR NULL,
     *      CONSTRAINT_NAME     VARCHAR NOT NULL,
     *      CHECK_CLAUSE        VARCHAR NOT NULL,
     * </pre>
     *
     * <b>Description:</b><p>
     *
     * <ol>
     * <li> A constraint is shown in this view if the authorization for the
     *      schema that contains the constraint is the current user or is a role
     *      assigned to the current user. <p>
     *
     * <li> The values of CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA and
     *      CONSTRAINT_NAME are the catalog name, unqualified schema name,
     *      and qualified identifier, respectively, of the constraint being
     *      described. <p>
     *
     * <li> Case: <p>
     *
     *      <table>
     *          <tr>
     *               <td valign="top" halign="left">a)</td>
     *               <td> If the character representation of the
     *                    &lt;search condition&gt; contained in the
     *                    &lt;check constraint definition&gt;,
     *                    &lt;domain constraint definition&gt;, or
     *                    &lt;assertion definition&gt; that defined
     *                    the check constraint being described can be
     *                    represented without truncation, then the
     *                    value of CHECK_CLAUSE is that character
     *                    representation. </td>
     *          </tr>
     *          <tr>
     *              <td align="top" halign="left">b)</td>
     *              <td>Otherwise, the value of CHECK_CLAUSE is the
     *                  null value.</td>
     *          </tr>
     *      </table>
     * </ol>
     *
     * @return Table
     */
Table CHECK_CONSTRAINTS(Session session) {
    Table t = sysTables[CHECK_CONSTRAINTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[CHECK_CONSTRAINTS]);
        addColumn(t, "CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "CHECK_CLAUSE", CHARACTER_DATA);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[CHECK_CONSTRAINTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 2, 1, 0 }, false);
        return t;
    }
    // column number mappings 
    final int constraint_catalog = 0;
    final int constraint_schema = 1;
    final int constraint_name = 2;
    final int check_clause = 3;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // calculated column values 
    // Intermediate holders 
    Iterator tables;
    Table table;
    Constraint[] tableConstraints;
    int constraintCount;
    Constraint constraint;
    Object[] row;
    // 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    while (tables.hasNext()) {
        table = (Table) tables.next();
        if (table.isView() || !session.getGrantee().isFullyAccessibleByRole(table.getName())) {
            continue;
        }
        tableConstraints = table.getConstraints();
        constraintCount = tableConstraints.length;
        for (int i = 0; i < constraintCount; i++) {
            constraint = tableConstraints[i];
            if (constraint.getConstraintType() != SchemaObject.ConstraintTypes.CHECK) {
                continue;
            }
            row = t.getEmptyRowData();
            row[constraint_catalog] = database.getCatalogName().name;
            row[constraint_schema] = table.getSchemaName().name;
            row[constraint_name] = constraint.getName().name;
            try {
                row[check_clause] = constraint.getCheckSQL();
            } catch (Exception e) {
            }
            t.insertSys(store, row);
        }
    }
    Iterator it = database.schemaManager.databaseObjectIterator(SchemaObject.DOMAIN);
    while (it.hasNext()) {
        Type domain = (Type) it.next();
        if (!domain.isDomainType()) {
            continue;
        }
        if (!session.getGrantee().isFullyAccessibleByRole(domain.getName())) {
            continue;
        }
        tableConstraints = domain.userTypeModifier.getConstraints();
        constraintCount = tableConstraints.length;
        for (int i = 0; i < constraintCount; i++) {
            constraint = tableConstraints[i];
            row = t.getEmptyRowData();
            row[constraint_catalog] = database.getCatalogName().name;
            row[constraint_schema] = domain.getSchemaName().name;
            row[constraint_name] = constraint.getName().name;
            try {
                row[check_clause] = constraint.getCheckSQL();
            } catch (Exception e) {
            }
            t.insertSys(store, row);
        }
    }
    return t;
}
