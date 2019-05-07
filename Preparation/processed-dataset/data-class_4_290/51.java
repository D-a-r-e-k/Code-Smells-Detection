void processAlterTableAddPrimaryKey(Table table, HsqlName name) {
    if (name == null) {
        name = session.database.nameManager.newAutoName("PK", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
    }
    OrderedHashSet set = readColumnNames(false);
    Constraint constraint = new Constraint(name, set, SchemaObject.ConstraintTypes.PRIMARY_KEY);
    constraint.setColumnsIndexes(table);
    session.commit(false);
    TableWorks tableWorks = new TableWorks(session, table);
    tableWorks.addPrimaryKey(constraint, name);
}
