Table UDT_PRIVILEGES(Session session) {
    Table t = sysTables[UDT_PRIVILEGES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[UDT_PRIVILEGES]);
        addColumn(t, "GRANTOR", SQL_IDENTIFIER);
        addColumn(t, "GRANTEE", SQL_IDENTIFIER);
        addColumn(t, "UDT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "UDT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "UDT_NAME", SQL_IDENTIFIER);
        addColumn(t, "PRIVILEGE_TYPE", CHARACTER_DATA);
        addColumn(t, "IS_GRANTABLE", YES_OR_NO);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[UDT_PRIVILEGES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3, 4 }, false);
        return t;
    }
    final int grantor = 0;
    final int grantee = 1;
    final int udt_catalog = 2;
    final int udt_schema = 3;
    final int udt_name = 4;
    final int privilege_type = 5;
    final int is_grantable = 6;
    PersistentStore store = session.sessionData.getRowStore(t);
    Iterator objects = database.schemaManager.databaseObjectIterator(SchemaObject.TYPE);
    OrderedHashSet grantees = session.getGrantee().getGranteeAndAllRolesWithPublic();
    while (objects.hasNext()) {
        SchemaObject object = (SchemaObject) objects.next();
        if (object.getType() != SchemaObject.TYPE) {
            continue;
        }
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
                Object[] row;
                row = t.getEmptyRowData();
                row[grantor] = right.getGrantor().getName().name;
                row[grantee] = right.getGrantee().getName().name;
                row[udt_catalog] = database.getCatalogName().name;
                row[udt_schema] = object.getSchemaName().name;
                row[udt_name] = object.getName().name;
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
