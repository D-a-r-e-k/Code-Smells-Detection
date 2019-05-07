/**
     * The DOMAINS view has one row for each domain. <p>
     *
     *
     * <pre class="SqlCodeExample">
     *
     * </pre>
     *
     * @return Table
     */
Table DOMAINS(Session session) {
    Table t = sysTables[DOMAINS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[DOMAINS]);
        addColumn(t, "DOMAIN_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "DOMAIN_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "DOMAIN_NAME", SQL_IDENTIFIER);
        addColumn(t, "DATA_TYPE", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_MAXIMUM_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_OCTET_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_SET_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_SET_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_SET_NAME", SQL_IDENTIFIER);
        addColumn(t, "COLLATION_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "COLLATION_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "COLLATION_NAME", SQL_IDENTIFIER);
        addColumn(t, "NUMERIC_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "NUMERIC_PRECISION_RADIX", CARDINAL_NUMBER);
        addColumn(t, "NUMERIC_SCALE", CARDINAL_NUMBER);
        addColumn(t, "DATETIME_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "INTERVAL_TYPE", CHARACTER_DATA);
        addColumn(t, "INTERVAL_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "DOMAIN_DEFAULT", CHARACTER_DATA);
        addColumn(t, "MAXIMUM_CARDINALITY", CARDINAL_NUMBER);
        addColumn(t, "DTD_IDENTIFIER", SQL_IDENTIFIER);
        addColumn(t, "DECLARED_DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "DECLARED_NUMERIC_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "DECLARED_NUMERIC_SCLAE", CARDINAL_NUMBER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[DOMAINS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 4, 5, 6 }, false);
        return t;
    }
    final int domain_catalog = 0;
    final int domain_schema = 1;
    final int domain_name = 2;
    final int data_type = 3;
    final int character_maximum_length = 4;
    final int character_octet_length = 5;
    final int character_set_catalog = 6;
    final int character_set_schema = 7;
    final int character_set_name = 8;
    final int collation_catalog = 9;
    final int collation_schema = 10;
    final int collation_name = 11;
    final int numeric_precision = 12;
    final int numeric_precision_radix = 13;
    final int numeric_scale = 14;
    final int datetime_precision = 15;
    final int interval_type = 16;
    final int interval_precision = 17;
    final int domain_default = 18;
    final int maximum_cardinality = 19;
    final int dtd_identifier = 20;
    final int declared_data_type = 21;
    final int declared_numeric_precision = 22;
    final int declared_numeric_scale = 23;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // 
    Iterator it = database.schemaManager.databaseObjectIterator(SchemaObject.DOMAIN);
    while (it.hasNext()) {
        Type type = (Type) it.next();
        if (!type.isDomainType()) {
            continue;
        }
        if (!session.getGrantee().isAccessible(type)) {
            continue;
        }
        Object[] row = t.getEmptyRowData();
        row[domain_catalog] = database.getCatalogName().name;
        row[domain_schema] = type.getSchemaName().name;
        row[domain_name] = type.getName().name;
        row[data_type] = type.getFullNameString();
        // common type block 
        if (type.isCharacterType()) {
            row[character_maximum_length] = ValuePool.getLong(type.precision);
            row[character_octet_length] = ValuePool.getLong(type.precision * 2);
            row[character_set_catalog] = database.getCatalogName().name;
            row[character_set_schema] = ((CharacterType) type).getCharacterSet().getSchemaName().name;
            row[character_set_name] = ((CharacterType) type).getCharacterSet().getName().name;
            row[collation_catalog] = database.getCatalogName().name;
            row[collation_schema] = ((CharacterType) type).getCollation().getSchemaName().name;
            row[collation_name] = ((CharacterType) type).getCollation().getName().name;
        } else if (type.isNumberType()) {
            row[numeric_precision] = ValuePool.getLong(((NumberType) type).getNumericPrecisionInRadix());
            row[declared_numeric_precision] = ValuePool.getLong(((NumberType) type).getNumericPrecisionInRadix());
            if (type.isExactNumberType()) {
                row[numeric_scale] = row[declared_numeric_scale] = ValuePool.getLong(type.scale);
            }
            row[numeric_precision_radix] = ValuePool.getLong(type.getPrecisionRadix());
        } else if (type.isBooleanType()) {
        } else if (type.isDateTimeType()) {
            row[datetime_precision] = ValuePool.getLong(type.scale);
        } else if (type.isIntervalType()) {
            row[data_type] = "INTERVAL";
            row[interval_type] = ((IntervalType) type).getQualifier(type.typeCode);
            row[interval_precision] = ValuePool.getLong(type.precision);
            row[datetime_precision] = ValuePool.getLong(type.scale);
        } else if (type.isBinaryType()) {
            row[character_maximum_length] = ValuePool.getLong(type.precision);
            row[character_octet_length] = ValuePool.getLong(type.precision);
        } else if (type.isBitType()) {
            row[character_maximum_length] = ValuePool.getLong(type.precision);
            row[character_octet_length] = ValuePool.getLong(type.precision);
        } else if (type.isArrayType()) {
            row[maximum_cardinality] = ValuePool.getLong(type.arrayLimitCardinality());
        }
        // end common block 
        Expression defaultExpression = type.userTypeModifier.getDefaultClause();
        if (defaultExpression != null) {
            row[domain_default] = defaultExpression.getSQL();
        }
        t.insertSys(store, row);
    }
    return t;
}
