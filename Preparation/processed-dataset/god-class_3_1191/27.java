/**
     * {@inheritDoc}
     * @see org.castor.persist.TransactionContext#query(
     *      org.exolab.castor.persist.LockEngine,
     *      org.exolab.castor.persist.spi.PersistenceQuery,
     *      org.exolab.castor.mapping.AccessMode, boolean)
     */
public final synchronized QueryResults query(final LockEngine engine, final PersistenceQuery query, final AccessMode accessMode, final boolean scrollable) throws PersistenceException {
    // Need to execute query at this point. This will result in a  
    // new result set from the query, or an exception.  
    query.execute(getConnection(engine), accessMode, scrollable);
    return new QueryResults(this, engine, query, accessMode, _db);
}
