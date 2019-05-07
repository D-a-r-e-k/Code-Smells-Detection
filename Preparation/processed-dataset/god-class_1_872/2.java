/**
     * Called by the cache administrator class when a cache is destroyed.
     *
     * @throws com.opensymphony.oscache.base.FinalizationException thrown when there was a problem finalizing the
     * listener. The cache administrator will catch and log this error.
     */
public void finialize() throws FinalizationException {
    try {
        if (log.isInfoEnabled()) {
            log.info("Shutting down JMS clustering...");
        }
        connection.close();
        if (log.isInfoEnabled()) {
            log.info("JMS clustering shutdown complete.");
        }
    } catch (JMSException e) {
        log.warn("A problem was encountered when closing the JMS connection", e);
    }
}
