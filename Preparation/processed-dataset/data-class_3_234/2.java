/**
     * Set up the various queues-of-queues used by the frontier. Override
     * in implementing subclasses to reduce or eliminate risk of queues
     * growing without bound. 
     */
protected void initQueuesOfQueues() {
    // small risk of OutOfMemoryError: if 'hold-queues' is false, 
    // readyClassQueues may grow in size without bound 
    readyClassQueues = new LinkedBlockingQueue<String>();
    // risk of OutOfMemoryError: in large crawls,  
    // inactiveQueues may grow in size without bound 
    inactiveQueues = new LinkedBlockingQueue<String>();
    // risk of OutOfMemoryError: in large crawls with queue max-budgets,  
    // inactiveQueues may grow in size without bound 
    retiredQueues = new LinkedBlockingQueue<String>();
    // small risk of OutOfMemoryError: in large crawls with many  
    // unresponsive queues, an unbounded number of snoozed queues  
    // may exist 
    snoozedClassQueues = Collections.synchronizedSortedSet(new TreeSet<WorkQueue>());
}
