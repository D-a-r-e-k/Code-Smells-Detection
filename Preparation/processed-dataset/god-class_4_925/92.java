/**
	 * Returns {@link org.quickserver.sql.DBPoolUtil} object if
	 * {@link org.quickserver.util.xmlreader.DBObjectPoolConfig} was set.
	 * @return DBPoolUtil object if object could be loaded, else will return <code>null</code>
	 * @since 1.3
	 */
public DBPoolUtil getDBPoolUtil() {
    return dBPoolUtil;
}
