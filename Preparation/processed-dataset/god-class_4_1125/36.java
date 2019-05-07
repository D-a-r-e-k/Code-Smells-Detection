Table PARAMETERS(Session session) {
    Table t = sysTables[PARAMETERS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[PARAMETERS]);
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        addColumn(t, "ORDINAL_POSITION", CARDINAL_NUMBER);
        addColumn(t, "PARAMETER_MODE", CHARACTER_DATA);
        addColumn(t, "IS_RESULT", YES_OR_NO);
        addColumn(t, "AS_LOCATOR", YES_OR_NO);
        addColumn(t, "PARAMETER_NAME", SQL_IDENTIFIER);
        // 
        addColumn(t, "FROM_SQL_SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "FROM_SQL_SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "FROM_SQL_SPECIFIC_NAME", SQL_IDENTIFIER);
        // 
        addColumn(t, "TO_SQL_SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TO_SQL_SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TO_SQL_SPECIFIC_NAME", SQL_IDENTIFIER);
        // 
        addColumn(t, "DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "CHARACTER_MAXIMUM_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_OCTET_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_SET_CATALOG", CHARACTER_DATA);
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
        addColumn(t, "UDT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "UDT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "UDT_NAME", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_NAME", SQL_IDENTIFIER);
        addColumn(t, "MAXIMUM_CARDINALITY", CARDINAL_NUMBER);
        addColumn(t, "DTD_IDENTIFIER", SQL_IDENTIFIER);
        addColumn(t, "DECLARED_DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "DECLARED_NUMERIC_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "DECLARED_NUMERIC_SCALE", CARDINAL_NUMBER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[PARAMETERS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2, 3 }, false);
        return t;
    }
    PersistentStore store = session.sessionData.getRowStore(t);
    // column number mappings 
    final int specific_cat = 0;
    final int specific_schem = 1;
    final int specific_name = 2;
    final int ordinal_position = 3;
    final int parameter_mode = 4;
    final int is_result = 5;
    final int as_locator = 6;
    final int parameter_name = 7;
    final int from_specific_catalog = 8;
    final int from_specific_schema = 9;
    final int from_specific_name = 10;
    final int to_specific_catalog = 11;
    final int to_specific_schema = 12;
    final int to_specific_name = 13;
    final int data_type = 14;
    final int character_maximum_length = 15;
    final int character_octet_length = 16;
    final int character_set_catalog = 17;
    final int character_set_schema = 18;
    final int character_set_name = 19;
    final int collation_catalog = 20;
    final int collation_schema = 21;
    final int collation_name = 22;
    final int numeric_precision = 23;
    final int numeric_precision_radix = 24;
    final int numeric_scale = 25;
    final int datetime_precision = 26;
    final int interval_type = 27;
    final int interval_precision = 28;
    final int udt_catalog = 29;
    final int udt_schema = 30;
    final int udt_name = 31;
    final int scope_catalog = 32;
    final int scope_schema = 33;
    final int scope_name = 34;
    final int maximum_cardinality = 35;
    final int dtd_identifier = 36;
    // intermediate holders 
    int columnCount;
    Iterator routines;
    RoutineSchema routineSchema;
    Routine routine;
    Object[] row;
    Type type;
    // Initialization 
    routines = database.schemaManager.databaseObjectIterator(SchemaObject.ROUTINE);
    while (routines.hasNext()) {
        routineSchema = (RoutineSchema) routines.next();
        if (!session.getGrantee().isAccessible(routineSchema)) {
            continue;
        }
        Routine[] specifics = routineSchema.getSpecificRoutines();
        for (int i = 0; i < specifics.length; i++) {
            routine = specifics[i];
            columnCount = routine.getParameterCount();
            for (int j = 0; j < columnCount; j++) {
                ColumnSchema column = routine.getParameter(j);
                type = column.getDataType();
                row = t.getEmptyRowData();
                row[specific_cat] = database.getCatalogName().name;
                row[specific_schem] = routine.getSchemaName().name;
                row[specific_name] = routine.getSpecificName().name;
                row[parameter_name] = column.getName().name;
                row[ordinal_position] = ValuePool.getLong(j + 1);
                switch(column.getParameterMode()) {
                    case SchemaObject.ParameterModes.PARAM_IN:
                        row[parameter_mode] = "IN";
                        break;
                    case SchemaObject.ParameterModes.PARAM_OUT:
                        row[parameter_mode] = "OUT";
                        break;
                    case SchemaObject.ParameterModes.PARAM_INOUT:
                        row[parameter_mode] = "INOUT";
                        break;
                }
                row[is_result] = "NO";
                row[as_locator] = "NO";
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
                if (type.isDistinctType()) {
                    row[udt_catalog] = database.getCatalogName().name;
                    row[udt_schema] = type.getSchemaName().name;
                    row[udt_name] = type.getName().name;
                }
                // end common block 
                t.insertSys(store, row);
            }
        }
    }
    return t;
}
