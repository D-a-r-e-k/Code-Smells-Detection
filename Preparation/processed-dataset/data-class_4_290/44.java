Statement compileAlterTableAddUniqueConstraint(Table table, HsqlName name) {
    if (name == null) {
        name = database.nameManager.newAutoName("CT", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
    }
    int[] cols = this.readColumnList(table, false);
    String sql = getLastPart();
    Object[] args = new Object[] { cols, name };
    return new StatementSchema(sql, StatementTypes.ALTER_TABLE, args, null, table.getName());
}
