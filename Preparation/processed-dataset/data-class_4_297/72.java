//------------------------------------------------------------------------------ 
// SQL SCHEMATA BASE TABLES 
/**
     * ROLE_AUTHORIZATION_DESCRIPTORS<p>
     *
     * <b>Function</b><p>
     *
     * Contains a representation of the role authorization descriptors.<p>
     * <b>Definition</b>
     *
     * <pre class="SqlCodeExample">
     * CREATE TABLE ROLE_AUTHORIZATION_DESCRIPTORS (
     *      ROLE_NAME INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *      GRANTEE INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *      GRANTOR INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *      IS_GRANTABLE INFORMATION_SCHEMA.CHARACTER_DATA
     *          CONSTRAINT ROLE_AUTHORIZATION_DESCRIPTORS_IS_GRANTABLE_CHECK
     *              CHECK ( IS_GRANTABLE IN
     *                  ( 'YES', 'NO' ) ),
     *          CONSTRAINT ROLE_AUTHORIZATION_DESCRIPTORS_PRIMARY_KEY
     *              PRIMARY KEY ( ROLE_NAME, GRANTEE ),
     *          CONSTRAINT ROLE_AUTHORIZATION_DESCRIPTORS_CHECK_ROLE_NAME
     *              CHECK ( ROLE_NAME IN
     *                  ( SELECT AUTHORIZATION_NAME
     *                      FROM AUTHORIZATIONS
     *                     WHERE AUTHORIZATION_TYPE = 'ROLE' ) ),
     *          CONSTRAINT ROLE_AUTHORIZATION_DESCRIPTORS_FOREIGN_KEY_AUTHORIZATIONS_GRANTOR
     *              FOREIGN KEY ( GRANTOR )
     *                  REFERENCES AUTHORIZATIONS,
     *          CONSTRAINT ROLE_AUTHORIZATION_DESCRIPTORS_FOREIGN_KEY_AUTHORIZATIONS_GRANTEE
     *              FOREIGN KEY ( GRANTEE )
     *                  REFERENCES AUTHORIZATIONS
     *      )
     * </pre>
     *
     * <b>Description</b><p>
     *
     * <ol>
     *      <li>The value of ROLE_NAME is the &lt;role name&gt; of some
     *          &lt;role granted&gt; by the &lt;grant role statement&gt; or
     *          the &lt;role name&gt; of a &lt;role definition&gt;. <p>
     *
     *      <li>The value of GRANTEE is an &lt;authorization identifier&gt;,
     *          possibly PUBLIC, or &lt;role name&gt; specified as a
     *          &lt;grantee&gt; contained in a &lt;grant role statement&gt;,
     *          or the &lt;authorization identifier&gt; of the current
     *          SQLsession when the &lt;role definition&gt; is executed. <p>
     *
     *      <li>The value of GRANTOR is the &lt;authorization identifier&gt;
     *          of the user or role who granted the role identified by
     *          ROLE_NAME to the user or role identified by the value of
     *          GRANTEE. <p>
     *
     *      <li>The values of IS_GRANTABLE have the following meanings:<p>
     *
     *      <table border cellpadding="3">
     *          <tr>
     *              <td nowrap>YES</td>
     *              <td nowrap>The described role is grantable.</td>
     *          <tr>
     *          <tr>
     *              <td nowrap>NO</td>
     *              <td nowrap>The described role is not grantable.</td>
     *          <tr>
     *      </table> <p>
     * </ol>
     *
     * @return Table
     */
Table ROLE_AUTHORIZATION_DESCRIPTORS(Session session) {
    Table t = sysTables[ROLE_AUTHORIZATION_DESCRIPTORS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROLE_AUTHORIZATION_DESCRIPTORS]);
        addColumn(t, "ROLE_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "GRANTOR", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        // not null 
        // true PK 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROLE_AUTHORIZATION_DESCRIPTORS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1 }, true);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    String grantorName = SqlInvariants.SYSTEM_AUTHORIZATION_NAME;
    Iterator grantees;
    Grantee granteeObject;
    String granteeName;
    Iterator roles;
    String roleName;
    String isGrantable;
    Object[] row;
    // Column number mappings 
    final int role_name = 0;
    final int grantee = 1;
    final int grantor = 2;
    final int is_grantable = 3;
    // Initialization 
    grantees = session.getGrantee().visibleGrantees().iterator();
    // 
    while (grantees.hasNext()) {
        granteeObject = (Grantee) grantees.next();
        granteeName = granteeObject.getNameString();
        roles = granteeObject.getDirectRoles().iterator();
        isGrantable = granteeObject.isAdmin() ? Tokens.T_YES : Tokens.T_NO;
        ;
        while (roles.hasNext()) {
            Grantee role = (Grantee) roles.next();
            row = t.getEmptyRowData();
            row[role_name] = role.getNameString();
            row[grantee] = granteeName;
            row[grantor] = grantorName;
            row[is_grantable] = isGrantable;
            t.insertSys(store, row);
        }
    }
    return t;
}
