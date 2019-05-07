/**
     * SCHEMATA<p>
     *
     * <b>Function</b><p>
     *
     * The SCHEMATA view has one row for each accessible schema. <p>
     *
     * <b>Definition</b><p>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE SCHEMATA (
     *      CATALOG_NAME INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *      SCHEMA_NAME INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *      SCHEMA_OWNER INFORMATION_SCHEMA.SQL_IDENTIFIER
     *          CONSTRAINT SCHEMA_OWNER_NOT_NULL
     *              NOT NULL,
     *      DEFAULT_CHARACTER_SET_CATALOG INFORMATION_SCHEMA.SQL_IDENTIFIER
     *          CONSTRAINT DEFAULT_CHARACTER_SET_CATALOG_NOT_NULL
     *              NOT NULL,
     *      DEFAULT_CHARACTER_SET_SCHEMA INFORMATION_SCHEMA.SQL_IDENTIFIER
     *          CONSTRAINT DEFAULT_CHARACTER_SET_SCHEMA_NOT_NULL
     *              NOT NULL,
     *      DEFAULT_CHARACTER_SET_NAME INFORMATION_SCHEMA.SQL_IDENTIFIER
     *          CONSTRAINT DEFAULT_CHARACTER_SET_NAME_NOT_NULL
     *              NOT NULL,
     *      SQL_PATH INFORMATION_SCHEMA.CHARACTER_DATA,
     *
     *      CONSTRAINT SCHEMATA_PRIMARY_KEY
     *          PRIMARY KEY ( CATALOG_NAME, SCHEMA_NAME ),
     *      CONSTRAINT SCHEMATA_FOREIGN_KEY_AUTHORIZATIONS
     *          FOREIGN KEY ( SCHEMA_OWNER )
     *              REFERENCES AUTHORIZATIONS,
     *      CONSTRAINT SCHEMATA_FOREIGN_KEY_CATALOG_NAMES
     *          FOREIGN KEY ( CATALOG_NAME )
     *              REFERENCES CATALOG_NAMES
     *      )
     * </pre>
     *
     * <b>Description</b><p>
     *
     * <ol>
     *      <li>The value of CATALOG_NAME is the name of the catalog of the
     *          schema described by this row.<p>
     *
     *      <li>The value of SCHEMA_NAME is the unqualified schema name of
     *          the schema described by this row.<p>
     *
     *      <li>The values of SCHEMA_OWNER are the authorization identifiers
     *          that own the schemata.<p>
     *
     *      <li>The values of DEFAULT_CHARACTER_SET_CATALOG,
     *          DEFAULT_CHARACTER_SET_SCHEMA, and DEFAULT_CHARACTER_SET_NAME
     *          are the catalog name, unqualified schema name, and qualified
     *          identifier, respectively, of the default character set for
     *          columns and domains in the schemata.<p>
     *
     *      <li>Case:<p>
     *          <ul>
     *              <li>If &lt;schema path specification&gt; was specified in
     *                  the &lt;schema definition&gt; that defined the schema
     *                  described by this row and the character representation
     *                  of the &lt;schema path specification&gt; can be
     *                  represented without truncation, then the value of
     *                  SQL_PATH is that character representation.<p>
     *
     *              <li>Otherwise, the value of SQL_PATH is the null value.
     *         </ul>
     * </ol>
     *
     * @return Table
     */
Table SCHEMATA(Session session) {
    Table t = sysTables[SCHEMATA];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SCHEMATA]);
        addColumn(t, "CATALOG_NAME", SQL_IDENTIFIER);
        addColumn(t, "SCHEMA_NAME", SQL_IDENTIFIER);
        addColumn(t, "SCHEMA_OWNER", SQL_IDENTIFIER);
        addColumn(t, "DEFAULT_CHARACTER_SET_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "DEFAULT_CHARACTER_SET_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "DEFAULT_CHARACTER_SET_NAME", SQL_IDENTIFIER);
        addColumn(t, "SQL_PATH", CHARACTER_DATA);
        // order: CATALOG_NAME, SCHEMA_NAME 
        // false PK, as rows may have NULL CATALOG_NAME 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SCHEMATA].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    Iterator schemas;
    String schema;
    String dcsSchema = SqlInvariants.INFORMATION_SCHEMA;
    String dcsName = ValuePool.getString("UTF16");
    String sqlPath = null;
    Grantee user = session.getGrantee();
    Object[] row;
    // column number mappings 
    final int schema_catalog = 0;
    final int schema_name = 1;
    final int schema_owner = 2;
    final int default_character_set_catalog = 3;
    final int default_character_set_schema = 4;
    final int default_character_set_name = 5;
    final int sql_path = 6;
    // Initialization 
    schemas = database.schemaManager.fullSchemaNamesIterator();
    // Do it. 
    while (schemas.hasNext()) {
        schema = (String) schemas.next();
        if (!user.hasSchemaUpdateOrGrantRights(schema)) {
            continue;
        }
        row = t.getEmptyRowData();
        row[schema_catalog] = database.getCatalogName().name;
        row[schema_name] = schema;
        row[schema_owner] = database.schemaManager.toSchemaOwner(schema).getNameString();
        row[default_character_set_catalog] = database.getCatalogName().name;
        row[default_character_set_schema] = dcsSchema;
        row[default_character_set_name] = dcsName;
        row[sql_path] = sqlPath;
        t.insertSys(store, row);
    }
    return t;
}
