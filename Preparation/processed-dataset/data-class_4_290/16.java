/**
     * Adds a list of temp constraints to a new table
     */
static Table addTableConstraintDefinitions(Session session, Table table, HsqlArrayList tempConstraints, HsqlArrayList constraintList, boolean addToSchema) {
    Constraint c = (Constraint) tempConstraints.get(0);
    String namePart = c.getName() == null ? null : c.getName().name;
    HsqlName indexName = session.database.nameManager.newAutoName("IDX", namePart, table.getSchemaName(), table.getName(), SchemaObject.INDEX);
    c.setColumnsIndexes(table);
    table.createPrimaryKey(indexName, c.core.mainCols, true);
    if (c.core.mainCols != null) {
        Constraint newconstraint = new Constraint(c.getName(), table, table.getPrimaryIndex(), SchemaObject.ConstraintTypes.PRIMARY_KEY);
        table.addConstraint(newconstraint);
        if (addToSchema) {
            session.database.schemaManager.addSchemaObject(newconstraint);
        }
    }
    for (int i = 1; i < tempConstraints.size(); i++) {
        c = (Constraint) tempConstraints.get(i);
        switch(c.constType) {
            case SchemaObject.ConstraintTypes.UNIQUE:
                {
                    c.setColumnsIndexes(table);
                    if (table.getUniqueConstraintForColumns(c.core.mainCols) != null) {
                        throw Error.error(ErrorCode.X_42522);
                    }
                    // create an autonamed index 
                    indexName = session.database.nameManager.newAutoName("IDX", c.getName().name, table.getSchemaName(), table.getName(), SchemaObject.INDEX);
                    Index index = table.createAndAddIndexStructure(session, indexName, c.core.mainCols, null, null, true, true, false);
                    Constraint newconstraint = new Constraint(c.getName(), table, index, SchemaObject.ConstraintTypes.UNIQUE);
                    table.addConstraint(newconstraint);
                    if (addToSchema) {
                        session.database.schemaManager.addSchemaObject(newconstraint);
                    }
                    break;
                }
            case SchemaObject.ConstraintTypes.FOREIGN_KEY:
                {
                    addForeignKey(session, table, c, constraintList);
                    break;
                }
            case SchemaObject.ConstraintTypes.CHECK:
                {
                    try {
                        c.prepareCheckConstraint(session, table, false);
                    } catch (HsqlException e) {
                        if (session.isProcessingScript()) {
                            break;
                        }
                        throw e;
                    }
                    table.addConstraint(c);
                    if (c.isNotNull()) {
                        ColumnSchema column = table.getColumn(c.notNullColumnIndex);
                        column.setNullable(false);
                        table.setColumnTypeVars(c.notNullColumnIndex);
                    }
                    if (addToSchema) {
                        session.database.schemaManager.addSchemaObject(c);
                    }
                    break;
                }
        }
    }
    return table;
}
