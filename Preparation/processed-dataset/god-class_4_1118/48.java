Statement compileAlterTableAddCheckConstraint(Table table, HsqlName name) {
    Constraint check;
    if (name == null) {
        name = database.nameManager.newAutoName("CT", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
    }
    check = new Constraint(name, null, SchemaObject.ConstraintTypes.CHECK);
    readCheckConstraintCondition(check);
    String sql = getLastPart();
    Object[] args = new Object[] { check };
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, args, null, table.getName());
}
