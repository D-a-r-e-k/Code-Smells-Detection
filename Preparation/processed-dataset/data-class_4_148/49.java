/**
     * Wrap the given <code>Connection</code> in a Proxy such that attributes 
     * that might be set will be restored before the connection is closed 
     * (and potentially restored to a pool).
     */
protected Connection getAttributeRestoringConnection(Connection conn) {
    return (Connection) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { Connection.class }, new AttributeRestoringConnectionInvocationHandler(conn));
}
