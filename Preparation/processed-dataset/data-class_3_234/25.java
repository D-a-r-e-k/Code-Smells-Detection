/**
     * Note that the previously emitted CrawlURI has completed
     * its processing (for now).
     *
     * The CrawlURI may be scheduled to retry, if appropriate,
     * and other related URIs may become eligible for release
     * via the next next() call, as a result of finished().
     *
     *  (non-Javadoc)
     * @see org.archive.crawler.framework.Frontier#finished(org.archive.crawler.datamodel.CrawlURI)
     */
public void finished(CrawlURI curi) {
    long now = System.currentTimeMillis();
    curi.incrementFetchAttempts();
    logLocalizedErrors(curi);
    WorkQueue wq = (WorkQueue) curi.getHolder();
    assert (wq.peek(this) == curi) : "unexpected peek " + wq;
    inProcessQueues.remove(wq, 1);
    if (includesRetireDirective(curi)) {
        // CrawlURI is marked to trigger retirement of its queue 
        curi.processingCleanup();
        wq.unpeek();
        wq.update(this, curi);
        // rewrite any changes 
        retireQueue(wq);
        return;
    }
    if (needsRetrying(curi)) {
        // Consider errors which can be retried, leaving uri atop queue 
        if (curi.getFetchStatus() != S_DEFERRED) {
            wq.expend(getCost(curi));
        }
        long delay_sec = retryDelayFor(curi);
        curi.processingCleanup();
        // lose state that shouldn't burden retry 
        synchronized (wq) {
            wq.unpeek();
            // TODO: consider if this should happen automatically inside unpeek() 
            wq.update(this, curi);
            // rewrite any changes 
            if (delay_sec > 0) {
                long delay_ms = delay_sec * 1000;
                snoozeQueue(wq, now, delay_ms);
            } else {
                reenqueueQueue(wq);
            }
        }
        // Let everyone interested know that it will be retried. 
        controller.fireCrawledURINeedRetryEvent(curi);
        doJournalRescheduled(curi);
        return;
    }
    // Curi will definitely be disposed of without retry, so remove from queue 
    wq.dequeue(this);
    decrementQueuedCount(1);
    log(curi);
    if (curi.isSuccess()) {
        totalProcessedBytes += curi.getRecordedSize();
        incrementSucceededFetchCount();
        // Let everyone know in case they want to do something before we strip the curi. 
        controller.fireCrawledURISuccessfulEvent(curi);
        doJournalFinishedSuccess(curi);
        wq.expend(getCost(curi));
    } else if (isDisregarded(curi)) {
        // Check for codes that mean that while we the crawler did 
        // manage to schedule it, it must be disregarded for some reason. 
        incrementDisregardedUriCount();
        // Let interested listeners know of disregard disposition. 
        controller.fireCrawledURIDisregardEvent(curi);
        doJournalDisregarded(curi);
        // if exception, also send to crawlErrors 
        if (curi.getFetchStatus() == S_RUNTIME_EXCEPTION) {
            Object[] array = { curi };
            controller.runtimeErrors.log(Level.WARNING, curi.getUURI().toString(), array);
        }
    } else {
        // In that case FAILURE, note & log 
        //Let interested listeners know of failed disposition. 
        this.controller.fireCrawledURIFailureEvent(curi);
        // if exception, also send to crawlErrors 
        if (curi.getFetchStatus() == S_RUNTIME_EXCEPTION) {
            Object[] array = { curi };
            this.controller.runtimeErrors.log(Level.WARNING, curi.getUURI().toString(), array);
        }
        incrementFailedFetchCount();
        // let queue note error 
        wq.noteError(((Integer) getUncheckedAttribute(curi, ATTR_ERROR_PENALTY_AMOUNT)).intValue());
        doJournalFinishedFailure(curi);
        wq.expend(getCost(curi));
    }
    long delay_ms = politenessDelayFor(curi);
    synchronized (wq) {
        if (delay_ms > 0) {
            snoozeQueue(wq, now, delay_ms);
        } else {
            reenqueueQueue(wq);
        }
    }
    curi.stripToMinimal();
    curi.processingCleanup();
}
