// ----------------------------------------------------------- Constructors 
/**
     * Class constructor. Initializes the fields with the default values.
     * The defaults are:
     * <pre>
     *      driverName = null;
     *      connectionURL = null;
     *      tableName = "access";
     *      remoteHostField = "remoteHost";
     *      userField = "userName";
     *      timestampField = "timestamp";
     *      virtualHostField = "virtualHost";
     *      methodField = "method";
     *      queryField = "query";
     *      statusField = "status";
     *      bytesField = "bytes";
     *      refererField = "referer";
     *      userAgentField = "userAgent";
     *      pattern = "common";
     *      resolveHosts = false;
     * </pre>
     */
public JDBCAccessLogValve() {
    super(false);
    driverName = null;
    connectionURL = null;
    tableName = "access";
    remoteHostField = "remoteHost";
    userField = "userName";
    timestampField = "timestamp";
    virtualHostField = "virtualHost";
    methodField = "method";
    queryField = "query";
    statusField = "status";
    bytesField = "bytes";
    refererField = "referer";
    userAgentField = "userAgent";
    pattern = "common";
    resolveHosts = false;
    conn = null;
    ps = null;
    currentTimeMillis = new java.util.Date().getTime();
}
