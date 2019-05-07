void method0() { 
// ----------------------------------------------------- Instance Variables 
/**
    * Use long contentLength as you have more 4 GB output.
    * @since 6.0.15
    */
protected boolean useLongContentLength = false;
/**
     * The connection username to use when trying to connect to the database.
     */
protected String connectionName = null;
/**
     * The connection URL to use when trying to connect to the database.
     */
protected String connectionPassword = null;
/**
     * Instance of the JDBC Driver class we use as a connection factory.
     */
protected Driver driver = null;
private String driverName;
private String connectionURL;
private String tableName;
private String remoteHostField;
private String userField;
private String timestampField;
private String virtualHostField;
private String methodField;
private String queryField;
private String statusField;
private String bytesField;
private String refererField;
private String userAgentField;
private String pattern;
private boolean resolveHosts;
private Connection conn;
private PreparedStatement ps;
private long currentTimeMillis;
/**
     * The descriptive information about this implementation.
     */
protected static final String info = "org.apache.catalina.valves.JDBCAccessLogValve/1.1";
/**
     * The string manager for this package.
     */
private static final StringManager sm = StringManager.getManager(Constants.Package);
}
