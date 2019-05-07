/**
     * The CHECK_CONSTRAINT_ROUTINE_USAGE view has one row for each
     * SQL-invoked routine identified as the subject routine of either a
     * &lt;routine invocation&gt;, a &lt;method reference&gt;, a
     * &lt;method invocation&gt;, or a &lt;static method invocation&gt;
     * contained in an &lt;assertion definition&gt;, a &lt;domain
     * constraint&gt;, or a &lt;table constraint definition&gt;. <p>
     *
     * <b>Definition:</b> <p>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE SYSTEM_CHECK_ROUTINE_USAGE (
     *      CONSTRAINT_CATALOG      VARCHAR NULL,
     *      CONSTRAINT_SCHEMA       VARCHAR NULL,
     *      CONSTRAINT_NAME         VARCHAR NOT NULL,
     *      SPECIFIC_CATALOG        VARCHAR NULL,
     *      SPECIFIC_SCHEMA         VARCHAR NULL,
     *      SPECIFIC_NAME           VARCHAR NOT NULL,
     *      UNIQUE( CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, CONSTRAINT_NAME,
     *              SPECIFIC_CATALOG, SPECIFIC_SCHEMA, SPECIFIC_NAME )
     * )
     * </pre>
     *
     * <b>Description:</b> <p>
     *
     * <ol>
     * <li> The CHECK_ROUTINE_USAGE table has one row for each
     *      SQL-invoked routine R identified as the subject routine of either a
     *      &lt;routine invocation&gt;, a &lt;method reference&gt;, a &lt;method
     *      invocation&gt;, or a &lt;static method invocation&gt; contained in
     *      an &lt;assertion definition&gt; or in the &lt;check constraint
     *      definition&gt; contained in either a &lt;domain constraint&gt; or a
     *      &lt;table constraint definition&gt;. <p>
     *
     * <li> The values of CONSTRAINT_CATALOG, CONSTRAINT_SCHEMA, and
     *      CONSTRAINT_NAME are the catalog name, unqualified schema name, and
     *      qualified identifier, respectively, of the assertion or check
     *     constraint being described. <p>
     *
     * <li> The values of SPECIFIC_CATALOG, SPECIFIC_SCHEMA, and SPECIFIC_NAME
     *      are the catalog name, unqualified schema name, and qualified
     *      identifier, respectively, of the specific name of R. <p>
     *
     * </ol>
     *
     * @return Table
     */
Table CHECK_CONSTRAINT_ROUTINE_USAGE(Session session) {
    Table t = sysTables[CHECK_CONSTRAINT_ROUTINE_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[CHECK_CONSTRAINT_ROUTINE_USAGE]);
        addColumn(t, "CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[CHECK_CONSTRAINT_ROUTINE_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5 }, false);
        return t;
    }
    // column number mappings 
    final int constraint_catalog = 0;
    final int constraint_schema = 1;
    final int constraint_name = 2;
    final int specific_catalog = 3;
    final int specific_schema = 4;
    final int specific_name = 5;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    Iterator constraints;
    Constraint constraint;
    OrderedHashSet references;
    RoutineSchema routine;
    Object[] row;
    constraints = database.schemaManager.databaseObjectIterator(SchemaObject.CONSTRAINT);
    while (constraints.hasNext()) {
        HsqlName constraintName = (HsqlName) constraints.next();
        if (constraintName.parent == null) {
            continue;
        }
        if (!session.getGrantee().isFullyAccessibleByRole(constraintName.parent)) {
            continue;
        }
        switch(constraintName.parent.type) {
            case SchemaObject.TABLE:
                {
                    Table table;
                    try {
                        table = (Table) database.schemaManager.getSchemaObject(constraintName.parent.name, constraintName.parent.schema.name, SchemaObject.TABLE);
                    } catch (Exception e) {
                        continue;
                    }
                    constraint = table.getConstraint(constraintName.name);
                    if (constraint.getConstraintType() != SchemaObject.ConstraintTypes.CHECK) {
                        continue;
                    }
                    break;
                }
            case SchemaObject.DOMAIN:
                {
                    Type domain;
                    try {
                        domain = (Type) database.schemaManager.getSchemaObject(constraintName.parent.name, constraintName.parent.schema.name, SchemaObject.DOMAIN);
                    } catch (Exception e) {
                        continue;
                    }
                    constraint = domain.userTypeModifier.getConstraint(constraintName.name);
                }
            default:
                continue;
        }
        references = constraint.getReferences();
        for (int i = 0; i < references.size(); i++) {
            HsqlName name = (HsqlName) references.get(i);
            if (name.type != SchemaObject.SPECIFIC_ROUTINE) {
                continue;
            }
            row = t.getEmptyRowData();
            row[constraint_catalog] = database.getCatalogName();
            row[constraint_schema] = constraint.getSchemaName().name;
            row[constraint_name] = constraint.getName().name;
            row[specific_catalog] = database.getCatalogName();
            row[specific_schema] = name.schema.name;
            row[specific_name] = name.name;
            t.insertSys(store, row);
        }
    }
    return t;
}
