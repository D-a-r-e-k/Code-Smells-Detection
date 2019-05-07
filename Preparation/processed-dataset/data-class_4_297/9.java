/**
     * APPLICABLE_ROLES<p>
     *
     * <b>Function</b><p>
     *
     * Identifies the applicable roles for the current user.<p>
     *
     * <b>Definition</b><p>
     *
     * <pre class="SqlCodeExample">
     * CREATE RECURSIVE VIEW APPLICABLE_ROLES ( GRANTEE, ROLE_NAME, IS_GRANTABLE ) AS
     *      ( ( SELECT GRANTEE, ROLE_NAME, IS_GRANTABLE
     *            FROM DEFINITION_SCHEMA.ROLE_AUTHORIZATION_DESCRIPTORS
     *           WHERE ( GRANTEE IN ( CURRENT_USER, 'PUBLIC' )
     *                OR GRANTEE IN ( SELECT ROLE_NAME
     *                                  FROM ENABLED_ROLES ) ) )
     *      UNION
     *      ( SELECT RAD.GRANTEE, RAD.ROLE_NAME, RAD.IS_GRANTABLE
     *          FROM DEFINITION_SCHEMA.ROLE_AUTHORIZATION_DESCRIPTORS RAD
     *          JOIN APPLICABLE_ROLES R
     *            ON RAD.GRANTEE = R.ROLE_NAME ) );
     *
     * GRANT SELECT ON TABLE APPLICABLE_ROLES
     *    TO PUBLIC WITH GRANT OPTION;
     * </pre>
     */
Table APPLICABLE_ROLES(Session session) {
    Table t = sysTables[APPLICABLE_ROLES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[APPLICABLE_ROLES]);
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        addColumn(t, "ROLE_NAME", SQL_IDENTIFIER);
        addColumn(t, "IS_GRANTABLE", SQL_IDENTIFIER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[APPLICABLE_ROLES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2 }, false);
        return t;
    }
    insertRoles(session, t, session.getGrantee(), session.isAdmin());
    return t;
}
