/**
     * For generated columns
     * <p>
     *
     * @return Table
     */
Table COLUMN_COLUMN_USAGE(Session session) {
    Table t = sysTables[COLUMN_COLUMN_USAGE];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[COLUMN_COLUMN_USAGE]);
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        addColumn(t, "COLUMN_NAME", SQL_IDENTIFIER);
        addColumn(t, "DEPENDENT_COLUMN", SQL_IDENTIFIER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[COLUMN_COLUMN_USAGE].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4 }, false);
        return t;
    }
    final int table_catalog = 0;
    final int table_schema = 1;
    final int table_name = 2;
    final int column_name = 3;
    final int dependent_column = 4;
    return t;
}
