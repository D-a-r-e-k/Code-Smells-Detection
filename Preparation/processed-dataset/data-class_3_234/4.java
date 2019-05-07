/* (non-Javadoc)
     * @see org.archive.crawler.frontier.AbstractFrontier#crawlEnded(java.lang.String)
     */
public void crawlEnded(String sExitMessage) {
    // Cleanup.  CrawlJobs persist after crawl has finished so undo any 
    // references. 
    if (this.alreadyIncluded != null) {
        this.alreadyIncluded.close();
        this.alreadyIncluded = null;
    }
    try {
        closeQueue();
    } catch (IOException e) {
        // FIXME exception handling 
        e.printStackTrace();
    }
    this.wakeTimer.cancel();
    this.allQueues.close();
    this.allQueues = null;
    this.inProcessQueues = null;
    this.readyClassQueues = null;
    this.snoozedClassQueues = null;
    this.inactiveQueues = null;
    this.retiredQueues = null;
    this.costAssignmentPolicy = null;
    // Clearing controller is a problem. We get NPEs in #preNext. 
    super.crawlEnded(sExitMessage);
    this.controller = null;
}
