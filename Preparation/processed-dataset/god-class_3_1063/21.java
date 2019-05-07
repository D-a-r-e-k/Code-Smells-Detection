/**
     * Activate an inactive queue, if any are available. 
     */
private void activateInactiveQueue() {
    Object key = this.inactiveQueues.poll();
    if (key == null) {
        return;
    }
    WorkQueue candidateQ = (WorkQueue) this.allQueues.get((String) key);
    if (candidateQ != null) {
        synchronized (candidateQ) {
            replenishSessionBalance(candidateQ);
            if (candidateQ.isOverBudget()) {
                // if still over-budget after an activation & replenishing, 
                // retire 
                retireQueue(candidateQ);
                return;
            }
            long now = System.currentTimeMillis();
            long delay_ms = candidateQ.getWakeTime() - now;
            if (delay_ms > 0) {
                // queue still due for snoozing 
                snoozeQueue(candidateQ, now, delay_ms);
                return;
            }
            candidateQ.setWakeTime(0);
            // clear obsolete wake time, if any 
            readyQueue(candidateQ);
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("ACTIVATED queue: " + candidateQ.getClassKey());
            }
        }
    }
}
