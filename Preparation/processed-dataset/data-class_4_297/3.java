Table SYSTEM_COMMENTS(Session session) {
    Table t = sysTables[SYSTEM_COMMENTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SYSTEM_COMMENTS]);
        addColumn(t, "OBJECT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "OBJECT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "OBJECT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "OBJECT_TYPE", SQL_IDENTIFIER);
        addColumn(t, "COLUMN_NAME", SQL_IDENTIFIER);
        addColumn(t, "COMMENT", CHARACTER_DATA);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SYSTEM_COMMENTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // column number mappings 
    final int catalog = 0;
    final int schema = 1;
    final int name = 2;
    final int type = 3;
    final int column_name = 4;
    final int remark = 5;
    Iterator it;
    Object[] row;
    // 
    DITableInfo ti = new DITableInfo();
    it = allTables();
    while (it.hasNext()) {
        Table table = (Table) it.next();
        if (!session.getGrantee().isAccessible(table)) {
            continue;
        }
        ti.setTable(table);
        int colCount = table.getColumnCount();
        for (int i = 0; i < colCount; i++) {
            ColumnSchema column = table.getColumn(i);
            if (column.getName().comment == null) {
                continue;
            }
            row = t.getEmptyRowData();
            row[catalog] = database.getCatalogName().name;
            row[schema] = table.getSchemaName().name;
            row[name] = table.getName().name;
            row[type] = "COLUMN";
            row[column_name] = column.getName().name;
            row[remark] = column.getName().comment;
            t.insertSys(store, row);
        }
        if (table.getTableType() != Table.SYSTEM_TABLE && table.getName().comment == null) {
            continue;
        }
        row = t.getEmptyRowData();
        row[catalog] = database.getCatalogName().name;
        row[schema] = table.getSchemaName().name;
        row[name] = table.getName().name;
        row[type] = table.isView() || table.getTableType() == Table.SYSTEM_TABLE ? "VIEW" : "TABLE";
        row[column_name] = null;
        row[remark] = ti.getRemark();
        t.insertSys(store, row);
    }
    it = database.schemaManager.databaseObjectIterator(SchemaObject.ROUTINE);
    while (it.hasNext()) {
        SchemaObject object = (SchemaObject) it.next();
        if (!session.getGrantee().isAccessible(object)) {
            continue;
        }
        if (object.getName().comment == null) {
            continue;
        }
        row = t.getEmptyRowData();
        row[catalog] = database.getCatalogName().name;
        row[schema] = object.getSchemaName().name;
        row[name] = object.getName().name;
        row[type] = "ROUTINE";
        row[column_name] = null;
        row[remark] = object.getName().comment;
        t.insertSys(store, row);
    }
    return t;
}
