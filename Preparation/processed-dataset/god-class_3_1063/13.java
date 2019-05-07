/**
     * Put the given queue on the retiredQueues queue
     * @param wq
     */
private void retireQueue(WorkQueue wq) {
    //        try { 
    retiredQueues.add(wq.getClassKey());
    decrementQueuedCount(wq.getCount());
    wq.setRetired(true);
    wq.setActive(this, false);
}
