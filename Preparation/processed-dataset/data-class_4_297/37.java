/**
     * <ol>
     * <li> A constraint is shown in this view if the user has table level
     * privilege of at lease one of the types, INSERT, UPDATE, DELETE,
     * REFERENCES or TRIGGER.
     * </ol>
     *
     * @return Table
     */
Table REFERENTIAL_CONSTRAINTS(Session session) {
    Table t = sysTables[REFERENTIAL_CONSTRAINTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[REFERENTIAL_CONSTRAINTS]);
        addColumn(t, "CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "UNIQUE_CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "UNIQUE_CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "UNIQUE_CONSTRAINT_NAME", SQL_IDENTIFIER);
        addColumn(t, "MATCH_OPTION", CHARACTER_DATA);
        // not null 
        addColumn(t, "UPDATE_RULE", CHARACTER_DATA);
        // not null 
        addColumn(t, "DELETE_RULE", CHARACTER_DATA);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[REFERENTIAL_CONSTRAINTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2 }, false);
        return t;
    }
    // column number mappings 
    final int constraint_catalog = 0;
    final int constraint_schema = 1;
    final int constraint_name = 2;
    final int unique_constraint_catalog = 3;
    final int unique_constraint_schema = 4;
    final int unique_constraint_name = 5;
    final int match_option = 6;
    final int update_rule = 7;
    final int delete_rule = 8;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    Iterator tables;
    Table table;
    Constraint[] constraints;
    Constraint constraint;
    Object[] row;
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    while (tables.hasNext()) {
        table = (Table) tables.next();
        if (table.isView() || !session.getGrantee().hasNonSelectTableRight(table)) {
            continue;
        }
        constraints = table.getConstraints();
        for (int i = 0; i < constraints.length; i++) {
            constraint = constraints[i];
            if (constraint.getConstraintType() != SchemaObject.ConstraintTypes.FOREIGN_KEY) {
                continue;
            }
            HsqlName uniqueName = constraint.getUniqueName();
            row = t.getEmptyRowData();
            row[constraint_catalog] = database.getCatalogName().name;
            row[constraint_schema] = constraint.getSchemaName().name;
            row[constraint_name] = constraint.getName().name;
            if (isAccessibleTable(session, constraint.getMain())) {
                row[unique_constraint_catalog] = database.getCatalogName().name;
                row[unique_constraint_schema] = uniqueName.schema.name;
                row[unique_constraint_name] = uniqueName.name;
            }
            row[match_option] = Tokens.T_NONE;
            row[update_rule] = constraint.getUpdateActionString();
            row[delete_rule] = constraint.getDeleteActionString();
            t.insertSys(store, row);
        }
    }
    return t;
}
