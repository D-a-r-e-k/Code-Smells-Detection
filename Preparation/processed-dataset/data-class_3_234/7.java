/**
     * Accept the given CandidateURI for scheduling, as it has
     * passed the alreadyIncluded filter. 
     * 
     * Choose a per-classKey queue and enqueue it. If this
     * item has made an unready queue ready, place that 
     * queue on the readyClassQueues queue. 
     * @param caUri CandidateURI.
     */
public void receive(CandidateURI caUri) {
    CrawlURI curi = asCrawlUri(caUri);
    applySpecialHandling(curi);
    sendToQueue(curi);
    // Update recovery log. 
    doJournalAdded(curi);
}
