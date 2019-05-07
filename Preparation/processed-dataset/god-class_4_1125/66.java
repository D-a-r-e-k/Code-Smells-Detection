/**
     * The USAGE_PRIVILEGES view has one row for each usage privilege
     * descriptor. <p>
     *
     * It effectively contains a representation of the usage privilege
     * descriptors. <p>
     *
     * <b>Definition:</b> <p>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE SYSTEM_USAGE_PRIVILEGES (
     *      GRANTOR         VARCHAR NOT NULL,
     *      GRANTEE         VARCHAR NOT NULL,
     *      OBJECT_CATALOG  VARCHAR NULL,
     *      OBJECT_SCHEMA   VARCHAR NULL,
     *      OBJECT_NAME     VARCHAR NOT NULL,
     *      OBJECT_TYPE     VARCHAR NOT NULL
     *
     *          CHECK ( OBJECT_TYPE IN (
     *                      'DOMAIN',
     *                      'CHARACTER SET',
     *                      'COLLATION',
     *                      'TRANSLATION',
     *                      'SEQUENCE' ) ),
     *
     *      IS_GRANTABLE    VARCHAR NOT NULL
     *
     *          CHECK ( IS_GRANTABLE IN ( 'YES', 'NO' ) ),
     *
     *      UNIQUE( GRANTOR, GRANTEE, OBJECT_CATALOG,
     *              OBJECT_SCHEMA, OBJECT_NAME, OBJECT_TYPE )
     * )
     * </pre>
     *
     * <b>Description:</b><p>
     *
     * <ol>
     * <li> The value of GRANTOR is the &lt;authorization identifier&gt; of the
     *      user or role who granted usage privileges on the object of the type
     *      identified by OBJECT_TYPE that is identified by OBJECT_CATALOG,
     *      OBJECT_SCHEMA, and OBJECT_NAME, to the user or role identified by the
     *      value of GRANTEE forthe usage privilege being described. <p>
     *
     * <li> The value of GRANTEE is the &lt;authorization identifier&gt; of some
     *      user or role, or PUBLIC to indicate all users, to whom the usage
     *      privilege being described is granted. <p>
     *
     * <li> The values of OBJECT_CATALOG, OBJECT_SCHEMA, and OBJECT_NAME are the
     *      catalog name, unqualified schema name, and qualified identifier,
     *      respectively, of the object to which the privilege applies. <p>
     *
     * <li> The values of OBJECT_TYPE have the following meanings: <p>
     *
     *      <table border cellpadding="3">
     *          <tr>
     *              <td nowrap>DOMAIN</td>
     *              <td nowrap>The object to which the privilege applies is
     *                         a domain.</td>
     *          <tr>
     *          <tr>
     *              <td nowrap>CHARACTER SET</td>
     *              <td nowrap>The object to which the privilege applies is a
     *                         character set.</td>
     *          <tr>
     *          <tr>
     *              <td nowrap>COLLATION</td>
     *              <td nowrap>The object to which the privilege applies is a
     *                         collation.</td>
     *          <tr>
     *          <tr>
     *              <td nowrap>TRANSLATION</td>
     *              <td nowrap>The object to which the privilege applies is a
     *                         transliteration.</td>
     *          <tr>
     *          <tr>
     *              <td nowrap>SEQUENCE</td>
     *              <td nowrap>The object to which the privilege applies is a
     *                         sequence generator.</td>
     *          <tr>
     *      </table> <p>
     *
     * <li> The values of IS_GRANTABLE have the following meanings: <p>
     *
     *      <table border cellpadding="3">
     *          <tr>
     *              <td nowrap>YES</td>
     *              <td nowrap>The privilege being described was granted
     *                         WITH GRANT OPTION and is thus grantable.</td>
     *          <tr>
     *          <tr>
     *              <td nowrap>NO</td>
     *              <td nowrap>The privilege being described was not granted
     *                  WITH GRANT OPTION and is thus not grantable.</td>
     *          <tr>
     *      </table> <p>
     * <ol>
     *
     * @return Table
     */
Table USAGE_PRIVILEGES(Session session) {
    Table t = sysTables[USAGE_PRIVILEGES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[USAGE_PRIVILEGES]);
        addColumn(t, "GRANTOR", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "OBJECT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "OBJECT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "OBJECT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "OBJECT_TYPE", CHARACTER_DATA);
        // not null 
        addColumn(t, "PRIVILEGE_TYPE", CHARACTER_DATA);
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        // not null 
        // order: COLUMN_NAME, PRIVILEGE 
        // for unique: GRANTEE, GRANTOR, TABLE_NAME, TABLE_SCHEM, TABLE_CAT 
        // false PK, as TABLE_SCHEM and/or TABLE_CAT may be null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[USAGE_PRIVILEGES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4, 5, 6, 7 }, false);
        return t;
    }
    // 
    Object[] row;
    // 
    final int grantor = 0;
    final int grantee = 1;
    final int object_catalog = 2;
    final int object_schema = 3;
    final int object_name = 4;
    final int object_type = 5;
    final int privilege_type = 6;
    final int is_grantable = 7;
    PersistentStore store = session.sessionData.getRowStore(t);
    Iterator objects = new WrapperIterator(database.schemaManager.databaseObjectIterator(SchemaObject.SEQUENCE), database.schemaManager.databaseObjectIterator(SchemaObject.COLLATION));
    objects = new WrapperIterator(objects, database.schemaManager.databaseObjectIterator(SchemaObject.CHARSET));
    objects = new WrapperIterator(objects, database.schemaManager.databaseObjectIterator(SchemaObject.DOMAIN));
    /*
        objects = new WrapperIterator(
            objects,
            database.schemaManager.databaseObjectIterator(SchemaObject.TYPE));
*/
    OrderedHashSet grantees = session.getGrantee().getGranteeAndAllRolesWithPublic();
    while (objects.hasNext()) {
        SchemaObject object = (SchemaObject) objects.next();
        for (int i = 0; i < grantees.size(); i++) {
            Grantee granteeObject = (Grantee) grantees.get(i);
            OrderedHashSet rights = granteeObject.getAllDirectPrivileges(object);
            OrderedHashSet grants = granteeObject.getAllGrantedPrivileges(object);
            if (!grants.isEmpty()) {
                grants.addAll(rights);
                rights = grants;
            }
            for (int j = 0; j < rights.size(); j++) {
                Right right = (Right) rights.get(j);
                Right grantableRight = right.getGrantableRights();
                row = t.getEmptyRowData();
                row[grantor] = right.getGrantor().getName().name;
                row[grantee] = right.getGrantee().getName().name;
                row[object_catalog] = database.getCatalogName().name;
                row[object_schema] = object.getSchemaName().name;
                row[object_name] = object.getName().name;
                row[object_type] = SchemaObjectSet.getName(object.getName().type);
                row[privilege_type] = Tokens.T_USAGE;
                row[is_grantable] = right.getGrantee() == object.getOwner() || grantableRight.isFull() ? Tokens.T_YES : Tokens.T_NO;
                ;
                try {
                    t.insertSys(store, row);
                } catch (HsqlException e) {
                }
            }
        }
    }
    return t;
}
