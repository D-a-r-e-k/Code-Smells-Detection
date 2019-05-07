/**
     * Put the given queue on the inactiveQueues queue
     * @param wq
     */
private void deactivateQueue(WorkQueue wq) {
    //        try { 
    wq.setSessionBalance(0);
    // zero out session balance 
    inactiveQueues.add(wq.getClassKey());
    wq.setActive(this, false);
}
