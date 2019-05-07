/**
     * Retrieves a <code>Table</code> object describing the TEXT TABLE objects
     * defined within this database. The table contains one row for each row
     * in the SYSTEM_TABLES table with a HSQLDB_TYPE of  TEXT . <p>
     *
     * Each row is a description of the attributes that defines its TEXT TABLE,
     * with the following columns:
     *
     * <pre class="SqlCodeExample">
     * TABLE_CAT                 VARCHAR   table's catalog name
     * TABLE_SCHEM               VARCHAR   table's simple schema name
     * TABLE_NAME                VARCHAR   table's simple name
     * DATA_SOURCE_DEFINITION    VARCHAR   the "spec" proption of the table's
     *                                     SET TABLE ... SOURCE DDL declaration
     * FILE_PATH                 VARCHAR   absolute file path.
     * FILE_ENCODING             VARCHAR   endcoding of table's text file
     * FIELD_SEPARATOR           VARCHAR   default field separator
     * VARCHAR_SEPARATOR         VARCAHR   varchar field separator
     * LONGVARCHAR_SEPARATOR     VARCHAR   longvarchar field separator
     * IS_IGNORE_FIRST           BOOLEAN   ignores first line of file?
     * IS_QUOTED                 BOOLEAN   fields are quoted if necessary?
     * IS_ALL_QUOTED             BOOLEAN   all fields are quoted?
     * IS_DESC                   BOOLEAN   read rows starting at end of file?
     * </pre> <p>
     *
     * @return a <code>Table</code> object describing the text attributes
     * of the accessible text tables defined within this database
     *
     */
Table SYSTEM_TEXTTABLES(Session session) {
    Table t = sysTables[SYSTEM_TEXTTABLES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SYSTEM_TEXTTABLES]);
        addColumn(t, "TABLE_CAT", SQL_IDENTIFIER);
        addColumn(t, "TABLE_SCHEM", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "DATA_SOURCE_DEFINTION", CHARACTER_DATA);
        addColumn(t, "FILE_PATH", CHARACTER_DATA);
        addColumn(t, "FILE_ENCODING", CHARACTER_DATA);
        addColumn(t, "FIELD_SEPARATOR", CHARACTER_DATA);
        addColumn(t, "VARCHAR_SEPARATOR", CHARACTER_DATA);
        addColumn(t, "LONGVARCHAR_SEPARATOR", CHARACTER_DATA);
        addColumn(t, "IS_IGNORE_FIRST", Type.SQL_BOOLEAN);
        addColumn(t, "IS_ALL_QUOTED", Type.SQL_BOOLEAN);
        addColumn(t, "IS_QUOTED", Type.SQL_BOOLEAN);
        addColumn(t, "IS_DESC", Type.SQL_BOOLEAN);
        // ------------------------------------------------------------ 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SYSTEM_TEXTTABLES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2 }, false);
        return t;
    }
    // column number mappings 
    final int itable_cat = 0;
    final int itable_schem = 1;
    final int itable_name = 2;
    final int idsd = 3;
    final int ifile_path = 4;
    final int ifile_enc = 5;
    final int ifs = 6;
    final int ivfs = 7;
    final int ilvfs = 8;
    final int iif = 9;
    final int iiq = 10;
    final int iiaq = 11;
    final int iid = 12;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // intermediate holders 
    Iterator tables;
    Table table;
    Object[] row;
    // Initialization 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    // Do it. 
    while (tables.hasNext()) {
        table = (Table) tables.next();
        PersistentStore currentStore = session.sessionData.getRowStore(t);
        if (!table.isText() || !isAccessibleTable(session, table)) {
            continue;
        }
        row = t.getEmptyRowData();
        row[itable_cat] = database.getCatalogName().name;
        row[itable_schem] = table.getSchemaName().name;
        row[itable_name] = table.getName().name;
        row[idsd] = ((TextTable) table).getDataSource();
        TextCache cache = (TextCache) currentStore.getCache();
        if (cache != null) {
            row[ifile_path] = FileUtil.getFileUtil().canonicalOrAbsolutePath(cache.getFileName());
            row[ifile_enc] = cache.stringEncoding;
            row[ifs] = cache.fs;
            row[ivfs] = cache.vs;
            row[ilvfs] = cache.lvs;
            row[iif] = ValuePool.getBoolean(cache.ignoreFirst);
            row[iiq] = ValuePool.getBoolean(cache.isQuoted);
            row[iiaq] = ValuePool.getBoolean(cache.isAllQuoted);
            row[iid] = ((TextTable) table).isDescDataSource() ? Boolean.TRUE : Boolean.FALSE;
        }
        t.insertSys(store, row);
    }
    return t;
}
