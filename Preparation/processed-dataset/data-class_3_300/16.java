/** 
     * {@inheritDoc}
     * This implementation closes all local connections.
     */
@Override
protected void notifySSLContextWasReset() {
    log.debug("freeThreadConnections called");
    closeThreadLocalConnections();
}
