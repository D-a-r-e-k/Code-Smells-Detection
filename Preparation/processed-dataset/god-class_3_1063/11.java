/**
     * Put the given queue on the readyClassQueues queue
     * @param wq
     */
private void readyQueue(WorkQueue wq) {
    try {
        wq.setActive(this, true);
        readyClassQueues.put(wq.getClassKey());
    } catch (InterruptedException e) {
        e.printStackTrace();
        System.err.println("unable to ready queue " + wq);
        // propagate interrupt up  
        throw new RuntimeException(e);
    }
}
