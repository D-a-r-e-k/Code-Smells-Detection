void method0() { 
/** Default period between logging stat values */
public static final Integer DEFAULT_STATISTICS_REPORT_INTERVAL = new Integer(20);
/** Attribute name for logging interval in seconds setting
     */
public static final String ATTR_STATS_INTERVAL = "interval-seconds";
/** A reference to the CrawlContoller of the crawl that we are to track
     * statistics for.
     */
protected transient CrawlController controller;
// Keep track of time. 
protected long crawlerStartTime;
protected long crawlerEndTime = -1;
// Until crawl ends, this value is -1. 
protected long crawlerPauseStarted = 0;
protected long crawlerTotalPausedTime = 0;
/** Timestamp of when this logger last wrote something to the log */
protected long lastLogPointTime;
protected volatile boolean shouldrun = true;
}
