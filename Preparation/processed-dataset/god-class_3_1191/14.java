/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getConnection(
     *      org.exolab.castor.persist.LockEngine)
     */
public final Connection getConnection(final LockEngine engine) throws ConnectionFailedException {
    Connection conn = _conns.get(engine);
    if (conn == null) {
        conn = createConnection(engine);
        _conns.put(engine, conn);
    }
    return conn;
}
