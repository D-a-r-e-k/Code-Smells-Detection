/**
     * protected
     * <p>
     * Resume (un-pause) all triggers - equivalent of calling <code>resumeTriggerGroup(group)</code>
     * on every group.
     * </p>
     * 
     * <p>
     * If any <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * 
     * @see #pauseAll(SchedulingContext)
     */
public void resumeAll(Connection conn, SchedulingContext ctxt) throws JobPersistenceException {
    String[] names = getTriggerGroupNames(conn, ctxt);
    for (int i = 0; i < names.length; i++) {
        resumeTriggerGroup(conn, ctxt, names[i]);
    }
    try {
        getDelegate().deletePausedTriggerGroup(conn, ALL_GROUPS_PAUSED);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't resume all trigger groups: " + e.getMessage(), e);
    }
}
