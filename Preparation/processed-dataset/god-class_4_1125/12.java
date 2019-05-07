/**
     *  SYSTEM_AUTHORIZATIONS<p>
     *
     *  <b>Function</b><p>
     *
     *  The AUTHORIZATIONS table has one row for each &lt;role name&gt; and
     *  one row for each &lt;authorization identifier &gt; referenced in the
     *  Information Schema. These are the &lt;role name&gt;s and
     *  &lt;authorization identifier&gt;s that may grant privileges as well as
     *  those that may create a schema, or currently own a schema created
     *  through a &lt;schema definition&gt;. <p>
     *
     *  <b>Definition</b><p>
     *
     *  <pre class="SqlCodeExample">
     *  CREATE TABLE AUTHORIZATIONS (
     *       AUTHORIZATION_NAME INFORMATION_SCHEMA.SQL_IDENTIFIER,
     *       AUTHORIZATION_TYPE INFORMATION_SCHEMA.CHARACTER_DATA
     *           CONSTRAINT AUTHORIZATIONS_AUTHORIZATION_TYPE_NOT_NULL
     *               NOT NULL
     *           CONSTRAINT AUTHORIZATIONS_AUTHORIZATION_TYPE_CHECK
     *               CHECK ( AUTHORIZATION_TYPE IN ( 'USER', 'ROLE' ) ),
     *           CONSTRAINT AUTHORIZATIONS_PRIMARY_KEY
     *               PRIMARY KEY (AUTHORIZATION_NAME)
     *       )
     *  </pre>
     *
     *  <b>Description</b><p>
     *
     *  <ol>
     *  <li> The values of AUTHORIZATION_TYPE have the following meanings:<p>
     *
     *  <table border cellpadding="3">
     *       <tr>
     *           <td nowrap>USER</td>
     *           <td nowrap>The value of AUTHORIZATION_NAME is a known
     *                      &lt;user identifier&gt;.</td>
     *       <tr>
     *       <tr>
     *           <td nowrap>NO</td>
     *           <td nowrap>The value of AUTHORIZATION_NAME is a &lt;role
     *                      name&gt; defined by a &lt;role definition&gt;.</td>
     *       <tr>
     *  </table> <p>
     *  </ol>
     *
     * @return Table
     */
Table AUTHORIZATIONS(Session session) {
    Table t = sysTables[AUTHORIZATIONS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[AUTHORIZATIONS]);
        addColumn(t, "AUTHORIZATION_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "AUTHORIZATION_TYPE", SQL_IDENTIFIER);
        // not null 
        // true PK 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[AUTHORIZATIONS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0 }, true);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // Intermediate holders 
    Iterator grantees;
    Grantee grantee;
    Object[] row;
    // initialization 
    grantees = session.getGrantee().visibleGrantees().iterator();
    // Do it. 
    while (grantees.hasNext()) {
        grantee = (Grantee) grantees.next();
        row = t.getEmptyRowData();
        row[0] = grantee.getNameString();
        row[1] = grantee.isRole() ? "ROLE" : "USER";
        t.insertSys(store, row);
    }
    return t;
}
