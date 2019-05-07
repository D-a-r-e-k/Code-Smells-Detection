/**
     * Return the next CrawlURI to be processed (and presumably
     * visited/fetched) by a a worker thread.
     *
     * Relies on the readyClassQueues having been loaded with
     * any work queues that are eligible to provide a URI. 
     *
     * @return next CrawlURI to be processed. Or null if none is available.
     *
     * @see org.archive.crawler.framework.Frontier#next()
     */
public CrawlURI next() throws InterruptedException, EndedException {
    while (true) {
        // loop left only by explicit return or exception 
        long now = System.currentTimeMillis();
        // Do common checks for pause, terminate, bandwidth-hold 
        preNext(now);
        // allow up-to-1 thread to fill readyClassQueues to target 
        if (readyFiller.tryAcquire()) {
            try {
                int activationsNeeded = targetSizeForReadyQueues() - readyClassQueues.size();
                while (activationsNeeded > 0 && !inactiveQueues.isEmpty()) {
                    activateInactiveQueue();
                    activationsNeeded--;
                }
            } finally {
                readyFiller.release();
            }
        }
        WorkQueue readyQ = null;
        Object key = readyClassQueues.poll(DEFAULT_WAIT, TimeUnit.MILLISECONDS);
        if (key != null) {
            readyQ = (WorkQueue) this.allQueues.get((String) key);
        }
        if (readyQ != null) {
            while (true) {
                // loop left by explicit return or break on empty 
                CrawlURI curi = null;
                synchronized (readyQ) {
                    curi = readyQ.peek(this);
                    if (curi != null) {
                        // check if curi belongs in different queue 
                        String currentQueueKey = getClassKey(curi);
                        if (currentQueueKey.equals(curi.getClassKey())) {
                            // curi was in right queue, emit 
                            noteAboutToEmit(curi, readyQ);
                            inProcessQueues.add(readyQ);
                            return curi;
                        }
                        // URI's assigned queue has changed since it 
                        // was queued (eg because its IP has become 
                        // known). Requeue to new queue. 
                        curi.setClassKey(currentQueueKey);
                        readyQ.dequeue(this);
                        decrementQueuedCount(1);
                        curi.setHolderKey(null);
                    } else {
                        // readyQ is empty and ready: it's exhausted 
                        // release held status, allowing any subsequent  
                        // enqueues to again put queue in ready 
                        readyQ.clearHeld();
                        break;
                    }
                }
                if (curi != null) {
                    // complete the requeuing begun earlier 
                    sendToQueue(curi);
                }
            }
        } else {
            // ReadyQ key wasn't in all queues: unexpected 
            if (key != null) {
                logger.severe("Key " + key + " in readyClassQueues but not allQueues");
            }
        }
        if (shouldTerminate) {
            // skip subsequent steps if already on last legs 
            throw new EndedException("shouldTerminate is true");
        }
        if (inProcessQueues.size() == 0) {
            // Nothing was ready or in progress or imminent to wake; ensure  
            // any piled-up pending-scheduled URIs are considered 
            this.alreadyIncluded.requestFlush();
        }
    }
}
