private void doUpdateOfMisfiredTrigger(Connection conn, SchedulingContext ctxt, Trigger trig, boolean forceState, String newStateIfNotComplete, boolean recovering) throws JobPersistenceException {
    Calendar cal = null;
    if (trig.getCalendarName() != null) {
        cal = retrieveCalendar(conn, ctxt, trig.getCalendarName());
    }
    schedSignaler.notifyTriggerListenersMisfired(trig);
    trig.updateAfterMisfire(cal);
    if (trig.getNextFireTime() == null) {
        storeTrigger(conn, ctxt, trig, null, true, STATE_COMPLETE, forceState, recovering);
    } else {
        storeTrigger(conn, ctxt, trig, null, true, newStateIfNotComplete, forceState, false);
    }
}
