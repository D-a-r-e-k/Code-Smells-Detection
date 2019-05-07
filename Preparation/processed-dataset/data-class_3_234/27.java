/**
     * Place the given queue into 'snoozed' state, ineligible to
     * supply any URIs for crawling, for the given amount of time. 
     * 
     * @param wq queue to snooze 
     * @param now time now in ms 
     * @param delay_ms time to snooze in ms
     */
private void snoozeQueue(WorkQueue wq, long now, long delay_ms) {
    long nextTime = now + delay_ms;
    wq.setWakeTime(nextTime);
    long snoozeToInactiveDelayMs = ((Long) getUncheckedAttribute(null, ATTR_SNOOZE_DEACTIVATE_MS)).longValue();
    if (delay_ms > snoozeToInactiveDelayMs && !inactiveQueues.isEmpty()) {
        deactivateQueue(wq);
    } else {
        synchronized (snoozedClassQueues) {
            snoozedClassQueues.add(wq);
            if (wq == snoozedClassQueues.first()) {
                this.nextWake = new WakeTask();
                this.wakeTimer.schedule(nextWake, delay_ms);
            }
        }
    }
}
