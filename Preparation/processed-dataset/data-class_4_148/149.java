/**
     * <p>
     * Cleanup the given database connection.  This means restoring
     * any modified auto commit or transaction isolation connection
     * attributes, and then closing the underlying connection.
     * </p>
     * 
     * <p>
     * This is separate from closeConnection() because the Spring 
     * integration relies on being able to overload closeConnection() and
     * expects the same connection back that it originally returned
     * from the datasource. 
     * </p>
     * 
     * @see #closeConnection(Connection)
     */
protected void cleanupConnection(Connection conn) {
    if (conn != null) {
        if (conn instanceof Proxy) {
            Proxy connProxy = (Proxy) conn;
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(connProxy);
            if (invocationHandler instanceof AttributeRestoringConnectionInvocationHandler) {
                AttributeRestoringConnectionInvocationHandler connHandler = (AttributeRestoringConnectionInvocationHandler) invocationHandler;
                connHandler.restoreOriginalAtributes();
                closeConnection(connHandler.getWrappedConnection());
                return;
            }
        }
        // Wan't a Proxy, or was a Proxy, but wasn't ours. 
        closeConnection(conn);
    }
}
