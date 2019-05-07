void processAlterTableAddCheckConstraint(Table table, HsqlName name) {
    Constraint check;
    if (name == null) {
        name = database.nameManager.newAutoName("CT", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
    }
    check = new Constraint(name, null, SchemaObject.ConstraintTypes.CHECK);
    readCheckConstraintCondition(check);
    session.commit(false);
    TableWorks tableWorks = new TableWorks(session, table);
    tableWorks.addCheckConstraint(check);
}
