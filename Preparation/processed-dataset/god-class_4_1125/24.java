/**
     * a DEFINITION_SCHEMA table. Not in the INFORMATION_SCHEMA list
     */
/*
    Table DATA_TYPE_DESCRIPTOR() {

        Table t = sysTables[DATA_TYPE_DESCRIPTOR];

        if (t == null) {
            t = createBlankTable(sysTableHsqlNames[DATA_TYPE_DESCRIPTOR]);

            addColumn(t, "OBJECT_CATALOG", SQL_IDENTIFIER);
            addColumn(t, "OBJECT_SCHEMA", SQL_IDENTIFIER);
            addColumn(t, "OBJECT_NAME", SQL_IDENTIFIER);
            addColumn(t, "OBJECT_TYPE", CHARACTER_DATA);
            addColumn(t, "DTD_IDENTIFIER", SQL_IDENTIFIER);
            addColumn(t, "DATA_TYPE", CHARACTER_DATA);
            addColumn(t, "CHARACTER_SET_CATALOG", SQL_IDENTIFIER);
            addColumn(t, "CHARACTER_SET_SCHEMA", SQL_IDENTIFIER);
            addColumn(t, "CHARACTER_SET_NAME", SQL_IDENTIFIER);
            addColumn(t, "CHARACTER_MAXIMUM_LENGTH", CARDINAL_NUMBER);
            addColumn(t, "CHARACTER_OCTET_LENGTH", CARDINAL_NUMBER);
            addColumn(t, "COLLATION_CATALOG", SQL_IDENTIFIER);
            addColumn(t, "COLLATION_SCHEMA", SQL_IDENTIFIER);
            addColumn(t, "COLLATION_NAME", SQL_IDENTIFIER);
            addColumn(t, "NUMERIC_PRECISION", CARDINAL_NUMBER);
            addColumn(t, "NUMERIC_PRECISION_RADIX", CARDINAL_NUMBER);
            addColumn(t, "NUMERIC_SCALE", CARDINAL_NUMBER);
            addColumn(t, "DECLARED_DATA_TYPE", CHARACTER_DATA);
            addColumn(t, "DECLARED_NUMERIC_PRECISION", CARDINAL_NUMBER);
            addColumn(t, "DECLARED_NUMERIC_SCLAE", CARDINAL_NUMBER);
            addColumn(t, "DATETIME_PRECISION", CARDINAL_NUMBER);
            addColumn(t, "INTERVAL_TYPE", CHARACTER_DATA);
            addColumn(t, "INTERVAL_PRECISION", CARDINAL_NUMBER);
            addColumn(t, "USER_DEFINED_TYPE_CATALOG", SQL_IDENTIFIER);
            addColumn(t, "USER_DEFINED_TYPE_SCHEMA", SQL_IDENTIFIER);
            addColumn(t, "USER_DEFINED_TYPE_NAME", SQL_IDENTIFIER);
            addColumn(t, "SCOPE_CATALOG", SQL_IDENTIFIER);
            addColumn(t, "SCOPE_SCHEMA", SQL_IDENTIFIER);
            addColumn(t, "SCOPE_NAME", SQL_IDENTIFIER);
            addColumn(t, "MAXIMUM_CARDINALITY", CARDINAL_NUMBER);
            t.createPrimaryKeyConstraint(null, new int[] {
                0, 1, 2, 4, 5, 6
            }, false);

            return t;
        }

        PersistentStore store =  session.sessionData.getRowStore(t);
        final int       object_catalog             = 0;
        final int       object_schema              = 1;
        final int       object_name                = 2;
        final int       object_type                = 3;
        final int       dtd_identifier             = 4;
        final int       data_type                  = 5;
        final int       character_set_catalog      = 6;
        final int       character_set_schema       = 7;
        final int       character_set_name         = 8;
        final int       character_maximum_length   = 9;
        final int       character_octet_length     = 10;
        final int       collation_catalog          = 11;
        final int       collation_schema           = 12;
        final int       collation_name             = 13;
        final int       numeric_precision          = 14;
        final int       numeric_precision_radix    = 15;
        final int       numeric_scale              = 16;
        final int       declared_data_type         = 17;
        final int       declared_numeric_precision = 18;
        final int       declared_numeric_scale     = 19;
        final int       datetime_precision         = 20;
        final int       interval_type              = 21;
        final int       interval_precision         = 22;
        final int       user_defined_type_catalog  = 23;
        final int       user_defined_type_schema   = 24;
        final int       user_defined_type_name     = 25;
        final int       scope_catalog              = 26;
        final int       scope_schema               = 27;
        final int       scope_name                 = 28;
        final int       maximum_cardinality        = 29;
        return t;
    }
*/
/**
     *
     * @return Table
     */
Table DOMAIN_CONSTRAINTS(Session session) {
    Table t = sysTables[DOMAIN_CONSTRAINTS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[DOMAIN_CONSTRAINTS]);
        addColumn(t, "CONSTRAINT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CONSTRAINT_NAME", SQL_IDENTIFIER);
        // not null 
        addColumn(t, "DOMAIN_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "DOMAIN_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "DOMAIN_NAME", SQL_IDENTIFIER);
        addColumn(t, "IS_DEFERRABLE", YES_OR_NO);
        addColumn(t, "INITIALLY_DEFERRED", YES_OR_NO);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[DOMAIN_CONSTRAINTS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 4, 5, 6 }, false);
        return t;
    }
    final int constraint_catalog = 0;
    final int constraint_schema = 1;
    final int constraint_name = 2;
    final int domain_catalog = 3;
    final int domain_schema = 4;
    final int domain_name = 5;
    final int is_deferrable = 6;
    final int initially_deferred = 7;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // 
    Iterator it = database.schemaManager.databaseObjectIterator(SchemaObject.DOMAIN);
    while (it.hasNext()) {
        Type domain = (Type) it.next();
        if (!domain.isDomainType()) {
            continue;
        }
        if (!session.getGrantee().isFullyAccessibleByRole(domain.getName())) {
            continue;
        }
        Constraint[] constraints = domain.userTypeModifier.getConstraints();
        for (int i = 0; i < constraints.length; i++) {
            Object[] data = t.getEmptyRowData();
            data[constraint_catalog] = data[domain_catalog] = database.getCatalogName().name;
            data[constraint_schema] = data[domain_schema] = domain.getSchemaName().name;
            data[constraint_name] = constraints[i].getName().name;
            data[domain_name] = domain.getName().name;
            data[is_deferrable] = Tokens.T_NO;
            data[initially_deferred] = Tokens.T_NO;
            t.insertSys(store, data);
        }
    }
    return t;
}
