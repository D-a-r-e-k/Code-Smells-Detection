Table ROUTINES(Session session) {
    Table t = sysTables[ROUTINES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[ROUTINES]);
        addColumn(t, "SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SPECIFIC_NAME", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_NAME", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_TYPE", CHARACTER_DATA);
        addColumn(t, "MODULE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "MODULE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "MODULE_NAME", SQL_IDENTIFIER);
        addColumn(t, "UDT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "UDT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "UDT_NAME", SQL_IDENTIFIER);
        addColumn(t, "DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "CHARACTER_MAXIMUM_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_OCTET_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_SET_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_SET_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_SET_NAME", SQL_IDENTIFIER);
        addColumn(t, "COLLATION_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "COLLATION_SCHEMA", SQL_IDENTIFIER);
        // 
        addColumn(t, "COLLATION_NAME", SQL_IDENTIFIER);
        addColumn(t, "NUMERIC_PRECISION", CARDINAL_NUMBER);
        // 
        addColumn(t, "NUMERIC_PRECISION_RADIX", CARDINAL_NUMBER);
        addColumn(t, "NUMERIC_SCALE", CARDINAL_NUMBER);
        addColumn(t, "DATETIME_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "INTERVAL_TYPE", CHARACTER_DATA);
        addColumn(t, "INTERVAL_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "TYPE_UDT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TYPE_UDT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TYPE_UDT_NAME", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_NAME", SQL_IDENTIFIER);
        // 
        addColumn(t, "MAXIMUM_CARDINALITY", CARDINAL_NUMBER);
        // (only for array tyes) 
        addColumn(t, "DTD_IDENTIFIER", SQL_IDENTIFIER);
        addColumn(t, "ROUTINE_BODY", CHARACTER_DATA);
        addColumn(t, "ROUTINE_DEFINITION", CHARACTER_DATA);
        addColumn(t, "EXTERNAL_NAME", CHARACTER_DATA);
        addColumn(t, "EXTERNAL_LANGUAGE", CHARACTER_DATA);
        addColumn(t, "PARAMETER_STYLE", CHARACTER_DATA);
        addColumn(t, "IS_DETERMINISTIC", YES_OR_NO);
        addColumn(t, "SQL_DATA_ACCESS", CHARACTER_DATA);
        addColumn(t, "IS_NULL_CALL", YES_OR_NO);
        addColumn(t, "SQL_PATH", CHARACTER_DATA);
        addColumn(t, "SCHEMA_LEVEL_ROUTINE", YES_OR_NO);
        // 
        addColumn(t, "MAX_DYNAMIC_RESULT_SETS", CARDINAL_NUMBER);
        addColumn(t, "IS_USER_DEFINED_CAST", YES_OR_NO);
        addColumn(t, "IS_IMPLICITLY_INVOCABLE", YES_OR_NO);
        addColumn(t, "SECURITY_TYPE", CHARACTER_DATA);
        addColumn(t, "TO_SQL_SPECIFIC_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "TO_SQL_SPECIFIC_SCHEMA", SQL_IDENTIFIER);
        // 
        addColumn(t, "TO_SQL_SPECIFIC_NAME", SQL_IDENTIFIER);
        addColumn(t, "AS_LOCATOR", YES_OR_NO);
        addColumn(t, "CREATED", TIME_STAMP);
        addColumn(t, "LAST_ALTERED", TIME_STAMP);
        addColumn(t, "NEW_SAVEPOINT_LEVEL", YES_OR_NO);
        addColumn(t, "IS_UDT_DEPENDENT", YES_OR_NO);
        addColumn(t, "RESULT_CAST_FROM_DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "RESULT_CAST_AS_LOCATOR", YES_OR_NO);
        addColumn(t, "RESULT_CAST_CHAR_MAX_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_CHAR_OCTET_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_CHAR_SET_CATALOG", CHARACTER_DATA);
        addColumn(t, "RESULT_CAST_CHAR_SET_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_CHARACTER_SET_NAME", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_COLLATION_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_COLLATION_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_COLLATION_NAME", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_NUMERIC_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_NUMERIC_RADIX", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_NUMERIC_SCALE", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_DATETIME_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_INTERVAL_TYPE", CHARACTER_DATA);
        addColumn(t, "RESULT_CAST_INTERVAL_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_TYPE_UDT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_TYPE_UDT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_TYPE_UDT_NAME", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_SCOPE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_SCOPE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_SCOPE_NAME", SQL_IDENTIFIER);
        addColumn(t, "RESULT_CAST_MAX_CARDINALITY", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_DTD_IDENTIFIER", CHARACTER_DATA);
        addColumn(t, "DECLARED_DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "DECLARED_NUMERIC_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "DECLARED_NUMERIC_SCALE", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_FROM_DECLARED_DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "RESULT_CAST_DECLARED_NUMERIC_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "RESULT_CAST_DECLARED_NUMERIC_SCALE", CARDINAL_NUMBER);
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[ROUTINES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 3, 4, 5, 0, 1, 2 }, false);
        return t;
    }
    // column number mappings 
    final int specific_catalog = 0;
    final int specific_schema = 1;
    final int specific_name = 2;
    final int routine_catalog = 3;
    final int routine_schema = 4;
    final int routine_name = 5;
    final int routine_type = 6;
    final int module_catalog = 7;
    final int module_schema = 8;
    final int module_name = 9;
    final int udt_catalog = 10;
    final int udt_schema = 11;
    final int udt_name = 12;
    final int data_type = 13;
    final int character_maximum_length = 14;
    final int character_octet_length = 15;
    final int character_set_catalog = 16;
    final int character_set_schema = 17;
    final int character_set_name = 18;
    final int collation_catalog = 19;
    final int collation_schema = 20;
    final int collation_name = 21;
    final int numeric_precision = 22;
    final int numeric_precision_radix = 23;
    final int numeric_scale = 24;
    final int datetime_precision = 25;
    final int interval_type = 26;
    final int interval_precision = 27;
    final int type_udt_catalog = 28;
    final int type_udt_schema = 29;
    final int type_udt_name = 30;
    final int scope_catalog = 31;
    final int scope_schema = 32;
    final int scope_name = 33;
    final int maximum_cardinality = 34;
    final int dtd_identifier = 35;
    final int routine_body = 36;
    final int routine_definition = 37;
    final int external_name = 38;
    final int external_language = 39;
    final int parameter_style = 40;
    final int is_deterministic = 41;
    final int sql_data_access = 42;
    final int is_null_call = 43;
    final int sql_path = 44;
    final int schema_level_routine = 45;
    final int max_dynamic_result_sets = 46;
    final int is_user_defined_cast = 47;
    final int is_implicitly_invocable = 48;
    final int security_type = 49;
    final int to_sql_specific_catalog = 50;
    final int to_sql_specific_schema = 51;
    final int to_sql_specific_name = 52;
    final int as_locator = 53;
    final int created = 54;
    final int last_altered = 55;
    final int new_savepoint_level = 56;
    final int is_udt_dependent = 57;
    final int result_cast_from_data_type = 58;
    final int result_cast_as_locator = 59;
    final int result_cast_char_max_length = 60;
    final int result_cast_char_octet_length = 61;
    final int result_cast_char_set_catalog = 62;
    final int result_cast_char_set_schema = 63;
    final int result_cast_character_set_name = 64;
    final int result_cast_collation_catalog = 65;
    final int result_cast_collation_schema = 66;
    final int result_cast_collation_name = 67;
    final int result_cast_numeric_precision = 68;
    final int result_cast_numeric_radix = 69;
    final int result_cast_numeric_scale = 70;
    final int result_cast_datetime_precision = 71;
    final int result_cast_interval_type = 72;
    final int result_cast_interval_precision = 73;
    final int result_cast_type_udt_catalog = 74;
    final int result_cast_type_udt_schema = 75;
    final int result_cast_type_udt_name = 76;
    final int result_cast_scope_catalog = 77;
    final int result_cast_scope_schema = 78;
    final int result_cast_scope_name = 79;
    final int result_cast_max_cardinality = 80;
    final int result_cast_dtd_identifier = 81;
    final int declared_data_type = 82;
    final int declared_numeric_precision = 83;
    final int declared_numeric_scale = 84;
    final int result_cast_from_declared_data_type = 85;
    final int result_cast_declared_numeric_precision = 86;
    final int result_cast_declared_numeric_scale = 87;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    Iterator it;
    Object[] row;
    it = database.schemaManager.databaseObjectIterator(SchemaObject.ROUTINE);
    while (it.hasNext()) {
        RoutineSchema routine = (RoutineSchema) it.next();
        if (!session.getGrantee().isAccessible(routine)) {
            continue;
        }
        Routine[] specifics = routine.getSpecificRoutines();
        for (int m = 0; m < specifics.length; m++) {
            row = t.getEmptyRowData();
            Routine specific = specifics[m];
            Type type = specific.isProcedure() ? null : specific.getReturnType();
            // 
            row[specific_catalog] = database.getCatalogName().name;
            row[specific_schema] = specific.getSchemaName().name;
            row[specific_name] = specific.getSpecificName().name;
            row[routine_catalog] = database.getCatalogName().name;
            row[routine_schema] = routine.getSchemaName().name;
            row[routine_name] = specific.getName().name;
            row[routine_type] = specific.isProcedure() ? Tokens.T_PROCEDURE : Tokens.T_FUNCTION;
            row[module_catalog] = null;
            row[module_schema] = null;
            row[module_name] = null;
            row[udt_catalog] = null;
            row[udt_schema] = null;
            row[udt_name] = null;
            row[data_type] = type == null ? null : type.getNameString();
            if (type != null) {
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
            }
            row[type_udt_catalog] = null;
            row[type_udt_schema] = null;
            row[type_udt_name] = null;
            row[scope_catalog] = null;
            row[scope_schema] = null;
            row[scope_name] = null;
            row[dtd_identifier] = null;
            //** 
            row[routine_body] = specific.getLanguage() == Routine.LANGUAGE_JAVA ? "EXTERNAL" : "SQL";
            row[routine_definition] = specific.getSQL();
            row[external_name] = specific.getLanguage() == Routine.LANGUAGE_JAVA ? specific.getMethod().getName() : null;
            row[external_language] = specific.getLanguage() == Routine.LANGUAGE_JAVA ? "JAVA" : null;
            row[parameter_style] = specific.getLanguage() == Routine.LANGUAGE_JAVA ? "JAVA" : null;
            row[is_deterministic] = specific.isDeterministic() ? "YES" : "NO";
            row[sql_data_access] = specific.getDataImpactString();
            row[is_null_call] = type == null ? null : specific.isNullInputOutput() ? "YES" : "NO";
            row[sql_path] = null;
            row[schema_level_routine] = "YES";
            row[max_dynamic_result_sets] = ValuePool.getLong(0);
            row[is_user_defined_cast] = type == null ? null : "NO";
            row[is_implicitly_invocable] = null;
            row[security_type] = "DEFINER";
            row[to_sql_specific_catalog] = null;
            row[to_sql_specific_schema] = null;
            row[to_sql_specific_name] = null;
            row[as_locator] = type == null ? null : "NO";
            row[created] = null;
            row[last_altered] = null;
            row[new_savepoint_level] = "YES";
            row[is_udt_dependent] = null;
            row[result_cast_from_data_type] = null;
            row[result_cast_as_locator] = null;
            row[result_cast_char_max_length] = null;
            row[result_cast_char_octet_length] = null;
            row[result_cast_char_set_catalog] = null;
            row[result_cast_char_set_schema] = null;
            row[result_cast_character_set_name] = null;
            row[result_cast_collation_catalog] = null;
            row[result_cast_collation_schema] = null;
            row[result_cast_collation_name] = null;
            row[result_cast_numeric_precision] = null;
            row[result_cast_numeric_radix] = null;
            row[result_cast_numeric_scale] = null;
            row[result_cast_datetime_precision] = null;
            row[result_cast_interval_type] = null;
            row[result_cast_interval_precision] = null;
            row[result_cast_type_udt_catalog] = null;
            row[result_cast_type_udt_schema] = null;
            row[result_cast_type_udt_name] = null;
            row[result_cast_scope_catalog] = null;
            row[result_cast_scope_schema] = null;
            row[result_cast_scope_name] = null;
            row[result_cast_max_cardinality] = null;
            row[result_cast_dtd_identifier] = null;
            row[declared_data_type] = row[data_type];
            row[declared_numeric_precision] = row[numeric_precision];
            row[declared_numeric_scale] = row[numeric_scale];
            row[result_cast_from_declared_data_type] = null;
            row[result_cast_declared_numeric_precision] = null;
            row[result_cast_declared_numeric_scale] = null;
            t.insertSys(store, row);
        }
    }
    return t;
}
