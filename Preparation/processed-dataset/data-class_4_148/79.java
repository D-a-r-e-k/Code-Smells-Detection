protected Trigger retrieveTrigger(Connection conn, String triggerName, String groupName) throws JobPersistenceException {
    try {
        Trigger trigger = getDelegate().selectTrigger(conn, triggerName, groupName);
        if (trigger == null) {
            return null;
        }
        // In case Trigger was BLOB, clear out any listeners that might  
        // have been serialized. 
        trigger.clearAllTriggerListeners();
        String[] listeners = getDelegate().selectTriggerListeners(conn, triggerName, groupName);
        for (int i = 0; i < listeners.length; ++i) {
            trigger.addTriggerListener(listeners[i]);
        }
        return trigger;
    } catch (Exception e) {
        throw new JobPersistenceException("Couldn't retrieve trigger: " + e.getMessage(), e);
    }
}
