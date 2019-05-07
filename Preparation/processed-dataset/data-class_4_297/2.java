/**
     * Retrieves a <code>Table</code> object describing the current
     * state of all row caching objects for the accessible
     * tables defined within this database. <p>
     *
     * Currently, the row caching objects for which state is reported are: <p>
     *
     * <OL>
     * <LI> the system-wide <code>Cache</code> object used by CACHED tables.
     * <LI> any <code>TextCache</code> objects in use by [TEMP] TEXT tables.
     * </OL> <p>
     *
     * Each row is a cache object state description with the following
     * columns: <p>
     *
     * <pre class="SqlCodeExample">
     * CACHE_FILE          CHARACTER_DATA   absolute path of cache data file
     * MAX_CACHE_SIZE      INTEGER   maximum allowable cached Row objects
     * MAX_CACHE_BYTE_SIZE INTEGER   maximum allowable size of cached Row objects
     * CACHE_LENGTH        INTEGER   number of data bytes currently cached
     * CACHE_SIZE          INTEGER   number of rows currently cached
     * FREE_BYTES          INTEGER   total bytes in available file allocation units
     * FREE_COUNT          INTEGER   total # of allocation units available
     * FREE_POS            INTEGER   largest file position allocated + 1
     * </pre> <p>
     *
     * <b>Notes:</b> <p>
     *
     * <code>TextCache</code> objects do not maintain a free list because
     * deleted rows are only marked deleted and never reused. As such, the
     * columns FREE_BYTES, SMALLEST_FREE_ITEM, LARGEST_FREE_ITEM, and
     * FREE_COUNT are always reported as zero for rows reporting on
     * <code>TextCache</code> objects. <p>
     *
     * Currently, CACHE_SIZE, FREE_BYTES, SMALLEST_FREE_ITEM, LARGEST_FREE_ITEM,
     * FREE_COUNT and FREE_POS are the only dynamically changing values.
     * All others are constant for the life of a cache object. In a future
     * release, other column values may also change over the life of a cache
     * object, as SQL syntax may eventually be introduced to allow runtime
     * modification of certain cache properties. <p>
     *
     * @return a description of the current state of all row caching
     *      objects associated with the accessible tables of the database
     */
Table SYSTEM_CACHEINFO(Session session) {
    Table t = sysTables[SYSTEM_CACHEINFO];
    if (t == null) {
        t = createBlankTable(sysTableHsqlNames[SYSTEM_CACHEINFO]);
        addColumn(t, "CACHE_FILE", CHARACTER_DATA);
        // not null 
        addColumn(t, "MAX_CACHE_COUNT", CARDINAL_NUMBER);
        // not null 
        addColumn(t, "MAX_CACHE_BYTES", CARDINAL_NUMBER);
        // not null 
        addColumn(t, "CACHE_SIZE", CARDINAL_NUMBER);
        // not null 
        addColumn(t, "CACHE_BYTES", CARDINAL_NUMBER);
        // not null 
        addColumn(t, "FILE_FREE_BYTES", CARDINAL_NUMBER);
        // not null 
        addColumn(t, "FILE_FREE_COUNT", CARDINAL_NUMBER);
        // not null 
        addColumn(t, "FILE_FREE_POS", CARDINAL_NUMBER);
        // not null 
        HsqlName name = HsqlNameManager.newInfoSchemaObjectName(sysTableHsqlNames[SYSTEM_CACHEINFO].name, false, SchemaObject.INDEX);
        t.createPrimaryKeyConstraint(name, new int[] { 0 }, true);
        return t;
    }
    // column number mappings 
    final int icache_file = 0;
    final int imax_cache_sz = 1;
    final int imax_cache_bytes = 2;
    final int icache_size = 3;
    final int icache_length = 4;
    final int ifree_bytes = 5;
    final int ifree_count = 6;
    final int ifree_pos = 7;
    // 
    PersistentStore store = session.sessionData.getRowStore(t);
    DataFileCache cache = null;
    Object[] row;
    HashSet cacheSet;
    Iterator caches;
    Iterator tables;
    Table table;
    int iFreeBytes;
    int iLargestFreeItem;
    long lSmallestFreeItem;
    // Initialization 
    cacheSet = new HashSet();
    // dynamic system tables are never cached 
    tables = database.schemaManager.databaseObjectIterator(SchemaObject.TABLE);
    while (tables.hasNext()) {
        table = (Table) tables.next();
        PersistentStore currentStore = session.sessionData.getRowStore(t);
        if (session.getGrantee().isFullyAccessibleByRole(table.getName())) {
            if (currentStore != null) {
                cache = currentStore.getCache();
            }
            if (cache != null) {
                cacheSet.add(cache);
            }
        }
    }
    caches = cacheSet.iterator();
    // Do it. 
    while (caches.hasNext()) {
        cache = (DataFileCache) caches.next();
        row = t.getEmptyRowData();
        row[icache_file] = FileUtil.getFileUtil().canonicalOrAbsolutePath(cache.getFileName());
        row[imax_cache_sz] = ValuePool.getLong(cache.capacity());
        row[imax_cache_bytes] = ValuePool.getLong(cache.bytesCapacity());
        row[icache_size] = ValuePool.getLong(cache.getCachedObjectCount());
        row[icache_length] = ValuePool.getLong(cache.getTotalCachedBlockSize());
        row[ifree_bytes] = ValuePool.getLong(cache.getTotalFreeBlockSize());
        row[ifree_count] = ValuePool.getLong(cache.getFreeBlockCount());
        row[ifree_pos] = ValuePool.getLong(cache.getFileFreePos());
        t.insertSys(store, row);
    }
    return t;
}
