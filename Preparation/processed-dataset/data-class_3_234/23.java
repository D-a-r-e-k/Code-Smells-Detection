/**
     * Enqueue the given queue to either readyClassQueues or inactiveQueues,
     * as appropriate.
     * 
     * @param wq
     */
private void reenqueueQueue(WorkQueue wq) {
    if (wq.isOverBudget()) {
        // if still over budget, deactivate 
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("DEACTIVATED queue: " + wq.getClassKey());
        }
        deactivateQueue(wq);
    } else {
        readyQueue(wq);
    }
}
