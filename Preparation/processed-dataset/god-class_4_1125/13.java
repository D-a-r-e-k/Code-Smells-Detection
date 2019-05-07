Table CHARACTER_SETS(Session session) {
    Table t = sysTables[CHARACTER_SETS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[CHARACTER_SETS]);
        addColumn(t, "CHARACTER_SET_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_SET_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_SET_NAME", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_REPERTOIRE", SQL_IDENTIFIER);
        addColumn(t, "FORM_OF_USE", SQL_IDENTIFIER);
        addColumn(t, "DEFAULT_COLLATE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "DEFAULT_COLLATE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "DEFAULT_COLLATE_NAME", SQL_IDENTIFIER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[CHARACTER_SETS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2 }, false);
        return t;
    }
    final int character_set_catalog = 0;
    final int character_set_schema = 1;
    final int character_set_name = 2;
    final int character_repertoire = 3;
    final int form_of_use = 4;
    final int default_collate_catalog = 5;
    final int default_collate_schema = 6;
    final int default_collate_name = 7;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    Iterator it = database.schemaManager.databaseObjectIterator(SchemaObject.CHARSET);
    while (it.hasNext()) {
        Charset charset = (Charset) it.next();
        if (!session.getGrantee().isAccessible(charset)) {
            continue;
        }
        Object[] data = t.getEmptyRowData();
        data[character_set_catalog] = database.getCatalogName().name;
        data[character_set_schema] = charset.getSchemaName().name;
        data[character_set_name] = charset.getName().name;
        data[character_repertoire] = "UCS";
        data[form_of_use] = "UTF16";
        data[default_collate_catalog] = data[character_set_catalog];
        if (charset.base == null) {
            data[default_collate_schema] = data[character_set_schema];
            data[default_collate_name] = data[character_set_name];
        } else {
            data[default_collate_schema] = charset.base.schema.name;
            data[default_collate_name] = charset.base.name;
        }
        t.insertSys(store, data);
    }
    return t;
}
