/**
     * Retrieves a <code>Table</code> object describing the capabilities
     * and operating parameter properties for the engine hosting this
     * database, as well as their applicability in terms of scope and
     * name space. <p>
     *
     * Reported properties include certain predefined <code>Database</code>
     * properties file values as well as certain database scope
     * attributes. <p>
     *
     * It is intended that all <code>Database</code> attributes and
     * properties that can be set via the database properties file,
     * JDBC connection properties or SQL SET/ALTER statements will
     * eventually be reported here or, where more applicable, in an
     * ANSI/ISO conforming feature info base table in the defintion
     * schema. <p>
     *
     * Currently, the database properties reported are: <p>
     *
     * <OL>
     *     <LI>hsqldb.cache_file_scale - the scaling factor used to translate data and index structure file pointers
     *     <LI>hsqldb.cache_scale - base-2 exponent scaling allowable cache row count
     *     <LI>hsqldb.cache_size_scale - base-2 exponent scaling allowable cache byte count
     *     <LI>hsqldb.cache_version -
     *     <LI>hsqldb.catalogs - whether to report the database catalog (database uri)
     *     <LI>hsqldb.compatible_version -
     *     <LI>hsqldb.files_readonly - whether the database is in files_readonly mode
     *     <LI>hsqldb.gc_interval - # new records forcing gc ({0|NULL}=>never)
     *     <LI>hsqldb.max_nio_scale - scale factor for cache nio mapped buffers
     *     <LI>hsqldb.nio_data_file - whether cache uses nio mapped buffers
     *     <LI>hsqldb.original_version -
     *     <LI>sql.enforce_strict_size - column length specifications enforced strictly (raise exception on overflow)?
     *     <LI>textdb.all_quoted - default policy regarding whether to quote all character field values
     *     <LI>textdb.cache_scale - base-2 exponent scaling allowable cache row count
     *     <LI>textdb.cache_size_scale - base-2 exponent scaling allowable cache byte count
     *     <LI>textdb.encoding - default TEXT table file encoding
     *     <LI>textdb.fs - default field separator
     *     <LI>textdb.vs - default varchar field separator
     *     <LI>textdb.lvs - default long varchar field separator
     *     <LI>textdb.ignore_first - default policy regarding whether to ignore the first line
     *     <LI>textdb.quoted - default policy regarding treatement character field values that _may_ require quoting
     *     <LI>IGNORECASE - create table VARCHAR_IGNORECASE?
     *     <LI>LOGSIZSE - # bytes to which REDO log grows before auto-checkpoint
     *     <LI>REFERENTIAL_INTEGITY - currently enforcing referential integrity?
     *     <LI>SCRIPTFORMAT - 0 : TEXT, 1 : BINARY, ...
     *     <LI>WRITEDELAY - does REDO log currently use buffered write strategy?
     * </OL> <p>
     *
     * @return table describing database and session operating parameters
     *      and capabilities
     */
Table SYSTEM_PROPERTIES(Session session) {
    Table t = sysTables[SYSTEM_PROPERTIES];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SYSTEM_PROPERTIES]);
        addColumn(t, "PROPERTY_SCOPE", CHARACTER_DATA);
        addColumn(t, "PROPERTY_NAMESPACE", CHARACTER_DATA);
        addColumn(t, "PROPERTY_NAME", CHARACTER_DATA);
        addColumn(t, "PROPERTY_VALUE", CHARACTER_DATA);
        addColumn(t, "PROPERTY_CLASS", CHARACTER_DATA);
        // order PROPERTY_SCOPE, PROPERTY_NAMESPACE, PROPERTY_NAME 
        // true PK 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SYSTEM_PROPERTIES].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0, 1, 2 }, true);
        return t;
    }
    // column number mappings 
    final int iscope = 0;
    final int ins = 1;
    final int iname = 2;
    final int ivalue = 3;
    final int iclass = 4;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    // calculated column values 
    String scope;
    String nameSpace;
    // intermediate holders 
    Object[] row;
    HsqlDatabaseProperties props;
    // First, we want the names and values for 
    // all JDBC capabilities constants 
    scope = "SESSION";
    props = database.getProperties();
    nameSpace = "database.properties";
    // boolean properties 
    Iterator it = props.getUserDefinedPropertyData().iterator();
    while (it.hasNext()) {
        Object[] metaData = (Object[]) it.next();
        row = t.getEmptyRowData();
        row[iscope] = scope;
        row[ins] = nameSpace;
        row[iname] = metaData[HsqlProperties.indexName];
        row[ivalue] = props.getProperty((String) row[iname]);
        row[iclass] = metaData[HsqlProperties.indexClass];
        t.insertSys(store, row);
    }
    row = t.getEmptyRowData();
    row[iscope] = scope;
    row[ins] = nameSpace;
    row[iname] = "SCRIPT FORMAT";
    try {
        row[ivalue] = ScriptWriterBase.LIST_SCRIPT_FORMATS[database.logger.getScriptType()];
    } catch (Exception e) {
    }
    row[iclass] = "String";
    t.insertSys(store, row);
    // write delay 
    row = t.getEmptyRowData();
    row[iscope] = scope;
    row[ins] = nameSpace;
    row[iname] = "WRITE DELAY";
    row[ivalue] = "" + database.logger.getWriteDelay();
    row[iclass] = "Integer";
    t.insertSys(store, row);
    // referential integrity 
    row = t.getEmptyRowData();
    row[iscope] = scope;
    row[ins] = nameSpace;
    row[iname] = "REFERENTIAL INTEGRITY";
    row[ivalue] = database.isReferentialIntegrity() ? "true" : "false";
    row[iclass] = "Boolean";
    t.insertSys(store, row);
    return t;
}
