void processAlterTableAddForeignKeyConstraint(Table table, HsqlName name) {
    if (name == null) {
        name = database.nameManager.newAutoName("FK", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
    }
    OrderedHashSet set = readColumnNames(false);
    Constraint c = readFKReferences(table, name, set);
    HsqlName mainTableName = c.getMainTableName();
    c.core.mainTable = database.schemaManager.getTable(session, mainTableName.name, mainTableName.schema.name);
    c.setColumnsIndexes(table);
    session.commit(false);
    TableWorks tableWorks = new TableWorks(session, table);
    tableWorks.addForeignKey(c);
}
