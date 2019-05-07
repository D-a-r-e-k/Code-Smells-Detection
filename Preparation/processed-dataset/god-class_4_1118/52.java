Statement compileAlterTableAddPrimaryKey(Table table, HsqlName name) {
    if (name == null) {
        name = session.database.nameManager.newAutoName("PK", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
    }
    OrderedHashSet set = readColumnNames(false);
    Constraint constraint = new Constraint(name, set, SchemaObject.ConstraintTypes.PRIMARY_KEY);
    constraint.setColumnsIndexes(table);
    String sql = getLastPart();
    Object[] args = new Object[] { constraint };
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, args, null, table.getName());
}
