void processAlterTableAddUniqueConstraint(Table table, HsqlName name) {
    if (name == null) {
        name = database.nameManager.newAutoName("CT", table.getSchemaName(), table.getName(), SchemaObject.CONSTRAINT);
    }
    int[] cols = this.readColumnList(table, false);
    session.commit(false);
    TableWorks tableWorks = new TableWorks(session, table);
    tableWorks.addUniqueConstraint(cols, name);
}
