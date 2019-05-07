/**
     * <p>
     * Pause all triggers - equivalent of calling <code>pauseTriggerGroup(group)</code>
     * on every group.
     * </p>
     * 
     * <p>
     * When <code>resumeAll()</code> is called (to un-pause), trigger misfire
     * instructions WILL be applied.
     * </p>
     * 
     * @see #resumeAll(SchedulingContext)
     * @see #pauseTriggerGroup(SchedulingContext, String)
     */
public void pauseAll(Connection conn, SchedulingContext ctxt) throws JobPersistenceException {
    String[] names = getTriggerGroupNames(conn, ctxt);
    for (int i = 0; i < names.length; i++) {
        pauseTriggerGroup(conn, ctxt, names[i]);
    }
    try {
        if (!getDelegate().isTriggerGroupPaused(conn, ALL_GROUPS_PAUSED)) {
            getDelegate().insertPausedTriggerGroup(conn, ALL_GROUPS_PAUSED);
        }
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't pause all trigger groups: " + e.getMessage(), e);
    }
}
