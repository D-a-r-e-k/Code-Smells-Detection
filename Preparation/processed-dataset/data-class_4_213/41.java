/** @see HarvestCoordinator#processSchedule(). */
public void processSchedule() {
    long now = System.currentTimeMillis();
    List<QueuedTargetInstanceDTO> theQueue = targetInstanceDao.getQueue();
    if (log.isDebugEnabled()) {
        log.debug("Start: Processing " + theQueue.size() + " entries from the queue.");
    }
    QueuedTargetInstanceDTO ti = null;
    Iterator<QueuedTargetInstanceDTO> it = theQueue.iterator();
    while (it.hasNext()) {
        ti = it.next();
        harvestOrQueue(ti);
    }
    if (log.isDebugEnabled()) {
        log.debug("Finished: Processing " + theQueue.size() + " entries from the queue. Took " + (System.currentTimeMillis() - now) + "ms");
    }
}
