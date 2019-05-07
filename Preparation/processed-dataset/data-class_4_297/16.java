/**
     * COLLATIONS<p>
     *
     * <b>Function<b><p>
     *
     * The COLLATIONS view has one row for each character collation
     * descriptor. <p>
     *
     * <b>Definition</b>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE COLLATIONS (
     *      COLLATION_CATALOG INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *      COLLATION_SCHEMA INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *      COLLATION_NAME INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *      PAD_ATTRIBUTE INFORMATION_SCHEMA.CHARACTER_DATA
     *          CONSTRAINT COLLATIONS_PAD_ATTRIBUTE_CHECK
     *              CHECK ( PAD_ATTRIBUTE IN
     *                  ( 'NO PAD', 'PAD SPACE' ) )
     * )
     * </pre>
     *
     * <b>Description</b><p>
     *
     * <ol>
     *      <li>The values of COLLATION_CATALOG, COLLATION_SCHEMA, and
     *          COLLATION_NAME are the catalog name, unqualified schema name,
     *          and qualified identifier, respectively, of the collation being
     *          described.<p>
     *
     *      <li>The values of PAD_ATTRIBUTE have the following meanings:<p>
     *
     *      <table border cellpadding="3">
     *          <tr>
     *              <td nowrap>NO PAD</td>
     *              <td nowrap>The collation being described has the NO PAD
     *                  characteristic.</td>
     *          <tr>
     *          <tr>
     *              <td nowrap>PAD</td>
     *              <td nowrap>The collation being described has the PAD SPACE
     *                         characteristic.</td>
     *          <tr>
     *      </table> <p>
     * </ol>
     *
     * @return Table
     */
Table COLLATIONS(Session session) {
    Table t = sysTables[COLLATIONS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[COLLATIONS]);
        addColumn(t, "COLLATION_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "COLLATION_SCHEMA", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "COLLATION_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "PAD_ATTRIBUTE", CHARACTER_DATA);
        // false PK, as rows may have NULL COLLATION_CATALOG 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[COLLATIONS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2 }, false);
        return t;
    }
    // Column number mappings 
    final int collation_catalog = 0;
    final int collation_schema = 1;
    final int collation_name = 2;
    final int pad_attribute = 3;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    Iterator collations;
    String collation;
    String collationSchema = SqlInvariants.PUBLIC_SCHEMA;
    String padAttribute = "NO PAD";
    Object[] row;
    // Initialization 
    collations = Collation.nameToJavaName.keySet().iterator();
    // Do it. 
    while (collations.hasNext()) {
        row = t.getEmptyRowData();
        collation = (String) collations.next();
        row[collation_catalog] = database.getCatalogName().name;
        row[collation_schema] = collationSchema;
        row[collation_name] = collation;
        row[pad_attribute] = padAttribute;
        t.insertSys(store, row);
    }
    return t;
}
