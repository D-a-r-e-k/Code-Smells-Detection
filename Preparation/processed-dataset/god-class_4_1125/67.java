Table USER_DEFINED_TYPES(Session session) {
    Table t = sysTables[USER_DEFINED_TYPES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[USER_DEFINED_TYPES]);
        addColumn(t, "USER_DEFINED_TYPE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "USER_DEFINED_TYPE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "USER_DEFINED_TYPE_NAME", SQL_IDENTIFIER);
        addColumn(t, "USER_DEFINED_TYPE_CATEGORY", SQL_IDENTIFIER);
        addColumn(t, "IS_INSTANTIABLE", YES_OR_NO);
        addColumn(t, "IS_FINAL", YES_OR_NO);
        addColumn(t, "ORDERING_FORM", SQL_IDENTIFIER);
        addColumn(t, "ORDERING_CATEGORY", SQL_IDENTIFIER);
        addColumn(t, "ORDERING_ROUTINE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "ORDERING_ROUTINE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "ORDERING_ROUTINE_NAME", SQL_IDENTIFIER);
        addColumn(t, "REFERENCE_TYPE", SQL_IDENTIFIER);
        addColumn(t, "DATA_TYPE", CHARACTER_DATA);
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
        addColumn(t, "SOURCE_DTD_IDENTIFIER", CHARACTER_DATA);
        addColumn(t, "REF_DTD_IDENTIFIER", CHARACTER_DATA);
        addColumn(t, "DECLARED_DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "DECLARED_NUMERIC_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "DECLARED_NUMERIC_SCALE", CARDINAL_NUMBER);
        addColumn(t, "EXTERNAL_NAME", CHARACTER_DATA);
        addColumn(t, "EXTERNAL_LANGUAGE", CHARACTER_DATA);
        addColumn(t, "JAVA_INTERFACE", CHARACTER_DATA);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[USER_DEFINED_TYPES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 4, 5, 6 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    final int user_defined_type_catalog = 0;
    final int user_defined_type_schema = 1;
    final int user_defined_type_name = 2;
    final int user_defined_type_category = 3;
    final int is_instantiable = 4;
    final int is_final = 5;
    final int ordering_form = 6;
    final int ordering_category = 7;
    final int ordering_routine_catalog = 8;
    final int ordering_routine_schema = 9;
    final int ordering_routine_name = 10;
    final int reference_type = 11;
    final int data_type = 12;
    final int character_maximum_length = 13;
    final int character_octet_length = 14;
    final int character_set_catalog = 15;
    final int character_set_schema = 16;
    final int character_set_name = 17;
    final int collation_catalog = 18;
    final int collation_schema = 19;
    final int collation_name = 20;
    final int numeric_precision = 21;
    final int numeric_precision_radix = 22;
    final int numeric_scale = 23;
    final int datetime_precision = 24;
    final int interval_type = 25;
    final int interval_precision = 26;
    final int source_dtd_identifier = 27;
    final int ref_dtd_identifier = 28;
    final int declared_data_type = 29;
    final int declared_numeric_precision = 30;
    final int declared_numeric_scale = 31;
    Iterator it = database.schemaManager.databaseObjectIterator(SchemaObject.TYPE);
    while (it.hasNext()) {
        Type type = (Type) it.next();
        if (!type.isDistinctType()) {
            continue;
        }
        Object[] row = t.getEmptyRowData();
        row[user_defined_type_catalog] = database.getCatalogName().name;
        row[user_defined_type_schema] = type.getSchemaName().name;
        row[user_defined_type_name] = type.getName().name;
        row[data_type] = type.getFullNameString();
        row[declared_data_type] = type.getFullNameString();
        row[user_defined_type_category] = "DISTINCT";
        row[is_instantiable] = "YES";
        row[is_final] = "YES";
        row[ordering_form] = "FULL";
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
        }
        // end common block 
        row[source_dtd_identifier] = row[user_defined_type_name];
        t.insertSys(store, row);
    }
    return t;
}
