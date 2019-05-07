/**
     * Restore a retired queue to the 'inactive' state. 
     * 
     * @param q
     */
private void unretireQueue(WorkQueue q) {
    deactivateQueue(q);
    q.setRetired(false);
    incrementQueuedUriCount(q.getCount());
}
