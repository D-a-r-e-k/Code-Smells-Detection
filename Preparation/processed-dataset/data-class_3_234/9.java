/**
     * Send a CrawlURI to the appropriate subqueue.
     * 
     * @param curi
     */
protected void sendToQueue(CrawlURI curi) {
    WorkQueue wq = getQueueFor(curi);
    synchronized (wq) {
        wq.enqueue(this, curi);
        if (!wq.isRetired()) {
            incrementQueuedUriCount();
        }
        if (!wq.isHeld()) {
            wq.setHeld();
            if (holdQueues() && readyClassQueues.size() >= targetSizeForReadyQueues()) {
                deactivateQueue(wq);
            } else {
                replenishSessionBalance(wq);
                readyQueue(wq);
            }
        }
        WorkQueue laq = longestActiveQueue;
        if (!wq.isRetired() && ((laq == null) || wq.getCount() > laq.getCount())) {
            longestActiveQueue = wq;
        }
    }
}
