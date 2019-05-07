/**
     * ENABLED_ROLES<p>
     *
     * <b>Function</b><p>
     *
     * Identify the enabled roles for the current SQL-session.<p>
     *
     * Definition<p>
     *
     * <pre class="SqlCodeExample">
     * CREATE RECURSIVE VIEW ENABLED_ROLES ( ROLE_NAME ) AS
     *      VALUES ( CURRENT_ROLE )
     *      UNION
     *      SELECT RAD.ROLE_NAME
     *        FROM DEFINITION_SCHEMA.ROLE_AUTHORIZATION_DESCRIPTORS RAD
     *        JOIN ENABLED_ROLES R
     *          ON RAD.GRANTEE = R.ROLE_NAME;
     *
     * GRANT SELECT ON TABLE ENABLED_ROLES
     *    TO PUBLIC WITH GRANT OPTION;
     * </pre>
     */
Table ENABLED_ROLES(Session session) {
    Table t = sysTables[ENABLED_ROLES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ENABLED_ROLES]);
        addColumn(t, "ROLE_NAME", SQL_IDENTIFIER);
        // true PK 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ENABLED_ROLES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0 }, true);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    Iterator grantees;
    Grantee grantee;
    Object[] row;
    // initialization 
    grantees = session.getGrantee().getAllRoles().iterator();
    while (grantees.hasNext()) {
        grantee = (Grantee) grantees.next();
        row = t.getEmptyRowData();
        row[0] = grantee.getNameString();
        t.insertSys(store, row);
    }
    return t;
}
