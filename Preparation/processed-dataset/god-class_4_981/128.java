// TODO: this really ought to return something like a FiredTriggerBundle, 
// so that the fireInstanceId doesn't have to be on the trigger... 
protected Trigger acquireNextTrigger(Connection conn, SchedulingContext ctxt, long noLaterThan) throws JobPersistenceException {
    do {
        try {
            Trigger nextTrigger = null;
            List keys = getDelegate().selectTriggerToAcquire(conn, noLaterThan, getMisfireTime());
            // No trigger is ready to fire yet. 
            if (keys == null || keys.size() == 0)
                return null;
            Iterator itr = keys.iterator();
            while (itr.hasNext()) {
                Key triggerKey = (Key) itr.next();
                int rowsUpdated = getDelegate().updateTriggerStateFromOtherState(conn, triggerKey.getName(), triggerKey.getGroup(), STATE_ACQUIRED, STATE_WAITING);
                // If our trigger was no longer in the expected state, try a new one. 
                if (rowsUpdated <= 0) {
                    continue;
                }
                nextTrigger = retrieveTrigger(conn, ctxt, triggerKey.getName(), triggerKey.getGroup());
                // If our trigger is no longer available, try a new one. 
                if (nextTrigger == null) {
                    continue;
                }
                break;
            }
            // if we didn't end up with a trigger to fire from that first 
            // batch, try again for another batch 
            if (nextTrigger == null) {
                continue;
            }
            nextTrigger.setFireInstanceId(getFiredTriggerRecordId());
            getDelegate().insertFiredTrigger(conn, nextTrigger, STATE_ACQUIRED, null);
            return nextTrigger;
        } catch (Exception e) {
            throw new JobPersistenceException("Couldn't acquire next trigger: " + e.getMessage(), e);
        }
    } while (true);
}
