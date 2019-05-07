/**
     * Open (if necessary) and return a database connection for use by
     * this AccessLogValve.
     *
     * @exception SQLException if a database error occurs
     */
protected void open() throws SQLException {
    // Do nothing if there is a database connection already open 
    if (conn != null)
        return;
    // Instantiate our database driver if necessary 
    if (driver == null) {
        try {
            Class<?> clazz = Class.forName(driverName);
            driver = (Driver) clazz.newInstance();
        } catch (Throwable e) {
            throw new SQLException(e.getMessage());
        }
    }
    // Open a new connection 
    Properties props = new Properties();
    props.put("autoReconnect", "true");
    if (connectionName != null)
        props.put("user", connectionName);
    if (connectionPassword != null)
        props.put("password", connectionPassword);
    conn = driver.connect(connectionURL, props);
    conn.setAutoCommit(true);
    if (pattern.equals("common")) {
        ps = conn.prepareStatement("INSERT INTO " + tableName + " (" + remoteHostField + ", " + userField + ", " + timestampField + ", " + queryField + ", " + statusField + ", " + bytesField + ") VALUES(?, ?, ?, ?, ?, ?)");
    } else if (pattern.equals("combined")) {
        ps = conn.prepareStatement("INSERT INTO " + tableName + " (" + remoteHostField + ", " + userField + ", " + timestampField + ", " + queryField + ", " + statusField + ", " + bytesField + ", " + virtualHostField + ", " + methodField + ", " + refererField + ", " + userAgentField + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    }
}
