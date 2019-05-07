/**
     * A method for logging current crawler state.
     *
     * This method will be called by run() at intervals specified in
     * the crawl order file.  It is also invoked when pausing or
     * stopping a crawl to capture the state at that point.  Default behavior is
     * call to {@link CrawlController#logProgressStatistics} so CrawlController
     * can act on progress statistics event.
     * <p>
     * It is recommended that for implementations of this method it be
     * carefully considered if it should be synchronized in whole or in
     * part
     * @param e Progress statistics event.
     */
protected synchronized void progressStatisticsEvent(final EventObject e) {
    this.controller.progressStatisticsEvent(e);
    // temporary workaround for  
    // [ 996161 ] Fix DNSJava issues (memory) -- replace with JNDI-DNS? 
    // http://sourceforge.net/support/tracker.php?aid=996161 
    Lookup.getDefaultCache(DClass.IN).clearCache();
}
