/**
     * <p>
     * Resume (un-pause) all of the <code>{@link org.quartz.Trigger}s</code>
     * in the given group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseTriggerGroup(Connection, SchedulingContext, String)
     */
public void resumeTriggerGroup(Connection conn, SchedulingContext ctxt, String groupName) throws JobPersistenceException {
    try {
        getDelegate().deletePausedTriggerGroup(conn, groupName);
        String[] trigNames = getDelegate().selectTriggersInGroup(conn, groupName);
        for (int i = 0; i < trigNames.length; i++) {
            resumeTrigger(conn, ctxt, trigNames[i], groupName);
        }
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't pause trigger group '" + groupName + "': " + e.getMessage(), e);
    }
}
