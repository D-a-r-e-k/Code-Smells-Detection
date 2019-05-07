/**
    * Shuts down the JavaGroups being managed by this listener. This
    * occurs once the cache is shut down and this listener is no longer
    * in use.
    *
    * @throws com.opensymphony.oscache.base.FinalizationException
    */
public synchronized void finialize() throws FinalizationException {
    if (log.isInfoEnabled()) {
        log.info("JavaGroups shutting down...");
    }
    // It's possible that the notification bus is null (CACHE-154)  
    if (bus != null) {
        bus.stop();
        bus = null;
    } else {
        log.warn("Notification bus wasn't initialized or finialize was invoked before!");
    }
    if (log.isInfoEnabled()) {
        log.info("JavaGroups shutdown complete.");
    }
}
