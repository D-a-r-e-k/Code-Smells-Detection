Table COLUMNS(Session session) {
    Table t = sysTables[COLUMNS];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[COLUMNS]);
        addColumn(t, "TABLE_CATALOG", SQL_IDENTIFIER);
        //0 
        addColumn(t, "TABLE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "TABLE_NAME", SQL_IDENTIFIER);
        addColumn(t, "COLUMN_NAME", SQL_IDENTIFIER);
        addColumn(t, "ORDINAL_POSITION", CARDINAL_NUMBER);
        addColumn(t, "COLUMN_DEFAULT", CHARACTER_DATA);
        addColumn(t, "IS_NULLABLE", YES_OR_NO);
        addColumn(t, "DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "CHARACTER_MAXIMUM_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_OCTET_LENGTH", CARDINAL_NUMBER);
        addColumn(t, "NUMERIC_PRECISION", CARDINAL_NUMBER);
        //10 
        addColumn(t, "NUMERIC_PRECISION_RADIX", CARDINAL_NUMBER);
        addColumn(t, "NUMERIC_SCALE", CARDINAL_NUMBER);
        addColumn(t, "DATETIME_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "INTERVAL_TYPE", CHARACTER_DATA);
        addColumn(t, "INTERVAL_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "CHARACTER_SET_CATALOG", CHARACTER_DATA);
        addColumn(t, "CHARACTER_SET_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "CHARACTER_SET_NAME", SQL_IDENTIFIER);
        addColumn(t, "COLLATION_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "COLLATION_SCHEMA", SQL_IDENTIFIER);
        //20 
        addColumn(t, "COLLATION_NAME", SQL_IDENTIFIER);
        addColumn(t, "DOMAIN_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "DOMAIN_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "DOMAIN_NAME", SQL_IDENTIFIER);
        addColumn(t, "UDT_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "UDT_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "UDT_NAME", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_CATALOG", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_SCHEMA", SQL_IDENTIFIER);
        addColumn(t, "SCOPE_NAME", SQL_IDENTIFIER);
        //30 
        addColumn(t, "MAXIMUM_CARDINALITY", CARDINAL_NUMBER);
        // (only for array tyes) 
        addColumn(t, "DTD_IDENTIFIER", SQL_IDENTIFIER);
        addColumn(t, "IS_SELF_REFERENCING", YES_OR_NO);
        addColumn(t, "IS_IDENTITY", YES_OR_NO);
        addColumn(t, "IDENTITY_GENERATION", CHARACTER_DATA);
        // ALWAYS / BY DEFAULT 
        addColumn(t, "IDENTITY_START", CHARACTER_DATA);
        addColumn(t, "IDENTITY_INCREMENT", CHARACTER_DATA);
        addColumn(t, "IDENTITY_MAXIMUM", CHARACTER_DATA);
        addColumn(t, "IDENTITY_MINIMUM", CHARACTER_DATA);
        addColumn(t, "IDENTITY_CYCLE", YES_OR_NO);
        //40 
        addColumn(t, "IS_GENERATED", CHARACTER_DATA);
        // ALWAYS / NEVER 
        addColumn(t, "GENERATION_EXPRESSION", CHARACTER_DATA);
        addColumn(t, "IS_UPDATABLE", YES_OR_NO);
        addColumn(t, "DECLARED_DATA_TYPE", CHARACTER_DATA);
        addColumn(t, "DECLARED_NUMERIC_PRECISION", CARDINAL_NUMBER);
        addColumn(t, "DECLARED_NUMERIC_SCALE", CARDINAL_NUMBER);
        // order: TABLE_SCHEM, TABLE_NAME, ORDINAL_POSITION 
        // added for unique: TABLE_CAT 
        // false PK, as TABLE_SCHEM and/or TABLE_CAT may be null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[COLUMNS].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 3, 2, 1, 4 }, false);
        return t;
    }
    // column number mappings 
    final int table_cat = 0;
    final int table_schem = 1;
    final int table_name = 2;
    final int column_name = 3;
    final int ordinal_position = 4;
    final int column_default = 5;
    final int is_nullable = 6;
    final int data_type = 7;
    final int character_maximum_length = 8;
    final int character_octet_length = 9;
    final int numeric_precision = 10;
    final int numeric_precision_radix = 11;
    final int numeric_scale = 12;
    final int datetime_precision = 13;
    final int interval_type = 14;
    final int interval_precision = 15;
    final int character_set_catalog = 16;
    final int character_set_schema = 17;
    final int character_set_name = 18;
    final int collation_catalog = 19;
    final int collation_schema = 20;
    final int collation_name = 21;
    final int domain_catalog = 22;
    final int domain_schema = 23;
    final int domain_name = 24;
    final int udt_catalog = 25;
    final int udt_schema = 26;
    final int udt_name = 27;
    final int scope_catalog = 28;
    final int scope_schema = 29;
    final int scope_name = 30;
    final int maximum_cardinality = 31;
    final int dtd_identifier = 32;
    final int is_self_referencing = 33;
    final int is_identity = 34;
    final int identity_generation = 35;
    final int identity_start = 36;
    final int identity_increment = 37;
    final int identity_maximum = 38;
    final int identity_minimum = 39;
    final int identity_cycle = 40;
    final int is_generated = 41;
    final int generation_expression = 42;
    final int is_updatable = 43;
    final int declared_data_type = 44;
    final int declared_numeric_precision = 45;
    final int declared_numeric_scale = 46;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // intermediate holders 
    int columnCount;
    Iterator tables;
    Table table;
    Object[] row;
    OrderedHashSet columnList;
    Type type;
    // Initialization 
    tables = allTables();
    while (tables.hasNext()) {
        table = (Table) tables.next();
        columnList = session.getGrantee().getColumnsForAllPrivileges(table);
        if (columnList.isEmpty()) {
            continue;
        }
        columnCount = table.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            ColumnSchema column = table.getColumn(i);
            type = column.getDataType();
            if (!columnList.contains(column.getName())) {
                continue;
            }
            row = t.getEmptyRowData();
            row[table_cat] = database.getCatalogName().name;
            row[table_schem] = table.getSchemaName().name;
            row[table_name] = table.getName().name;
            row[column_name] = column.getName().name;
            row[ordinal_position] = ValuePool.getLong(i + 1);
            row[column_default] = column.getDefaultSQL();
            row[is_nullable] = column.isNullable() ? "YES" : "NO";
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
            if (type.isDomainType()) {
                row[domain_catalog] = database.getCatalogName().name;
                row[domain_schema] = type.getSchemaName().name;
                row[domain_name] = type.getName().name;
            }
            if (type.isDistinctType()) {
                row[udt_catalog] = database.getCatalogName().name;
                row[udt_schema] = type.getSchemaName().name;
                row[udt_name] = type.getName().name;
            }
            row[scope_catalog] = null;
            row[scope_schema] = null;
            row[scope_name] = null;
            row[dtd_identifier] = null;
            row[is_self_referencing] = null;
            row[is_identity] = column.isIdentity() ? "YES" : "NO";
            if (column.isIdentity()) {
                NumberSequence sequence = column.getIdentitySequence();
                row[identity_generation] = sequence.isAlways() ? "ALWAYS" : "BY DEFAULT";
                row[identity_start] = Long.toString(sequence.getStartValue());
                row[identity_increment] = Long.toString(sequence.getIncrement());
                row[identity_maximum] = Long.toString(sequence.getMaxValue());
                row[identity_minimum] = Long.toString(sequence.getMinValue());
                row[identity_cycle] = sequence.isCycle() ? "YES" : "NO";
            }
            row[is_generated] = "NEVER";
            if (column.isGenerated()) {
                row[is_generated] = "ALWAYS";
                row[generation_expression] = column.getGeneratingExpression().getSQL();
            }
            row[is_updatable] = table.isWritable() ? "YES" : "NO";
            row[declared_data_type] = row[data_type];
            if (type.isNumberType()) {
                row[declared_numeric_precision] = row[numeric_precision];
                row[declared_numeric_scale] = row[numeric_scale];
            }
            t.insertSys(store, row);
        }
    }
    return t;
}
