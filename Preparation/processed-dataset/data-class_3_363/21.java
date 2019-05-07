/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#getConnectionInfo(
     *      org.exolab.castor.persist.LockEngine)
     */
public final DbMetaInfo getConnectionInfo(final LockEngine engine) throws PersistenceException {
    Connection conn = getConnection(engine);
    if (_dbInfo == null) {
        _dbInfo = new DbMetaInfo(conn);
    }
    return _dbInfo;
}
