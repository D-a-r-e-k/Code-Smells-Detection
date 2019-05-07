/**
     * Initializes the Frontier, given the supplied CrawlController.
     *
     * @see org.archive.crawler.framework.Frontier#initialize(org.archive.crawler.framework.CrawlController)
     */
public void initialize(CrawlController c) throws FatalConfigurationException, IOException {
    // Call the super method. It sets up frontier journalling. 
    super.initialize(c);
    this.controller = c;
    initQueuesOfQueues();
    this.targetSizeForReadyQueues = (Integer) getUncheckedAttribute(null, ATTR_TARGET_READY_QUEUES_BACKLOG);
    if (this.targetSizeForReadyQueues < 1) {
        this.targetSizeForReadyQueues = 1;
    }
    this.wakeTimer = new Timer("waker for " + c.toString());
    try {
        if (workQueueDataOnDisk() && getQueueAssignmentPolicy(null).maximumNumberOfKeys() >= 0 && getQueueAssignmentPolicy(null).maximumNumberOfKeys() <= MAX_QUEUES_TO_HOLD_ALLQUEUES_IN_MEMORY) {
            this.allQueues = new ObjectIdentityMemCache<WorkQueue>(701, .9f, 100);
        } else {
            this.allQueues = c.getBigMap("allqueues", WorkQueue.class);
            if (logger.isLoggable(Level.FINE)) {
                Iterator<String> i = this.allQueues.keySet().iterator();
                try {
                    for (; i.hasNext(); ) {
                        logger.fine((String) i.next());
                    }
                } finally {
                    StoredIterator.close(i);
                }
            }
        }
        this.alreadyIncluded = createAlreadyIncluded();
        initQueue();
    } catch (IOException e) {
        e.printStackTrace();
        throw (FatalConfigurationException) new FatalConfigurationException(e.getMessage()).initCause(e);
    } catch (Exception e) {
        e.printStackTrace();
        throw (FatalConfigurationException) new FatalConfigurationException(e.getMessage()).initCause(e);
    }
    initCostPolicy();
    loadSeeds();
}
