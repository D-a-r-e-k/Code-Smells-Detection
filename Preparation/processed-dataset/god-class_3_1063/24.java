/**
     * Wake any queues sitting in the snoozed queue whose time has come.
     */
void wakeQueues() {
    synchronized (snoozedClassQueues) {
        long now = System.currentTimeMillis();
        long nextWakeDelay = 0;
        int wokenQueuesCount = 0;
        while (true) {
            if (snoozedClassQueues.isEmpty()) {
                return;
            }
            WorkQueue peek = (WorkQueue) snoozedClassQueues.first();
            nextWakeDelay = peek.getWakeTime() - now;
            if (nextWakeDelay <= 0) {
                snoozedClassQueues.remove(peek);
                peek.setWakeTime(0);
                reenqueueQueue(peek);
                wokenQueuesCount++;
            } else {
                break;
            }
        }
        this.nextWake = new WakeTask();
        this.wakeTimer.schedule(nextWake, nextWakeDelay);
    }
}
