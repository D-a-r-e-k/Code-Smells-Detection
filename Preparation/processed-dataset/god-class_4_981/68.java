protected boolean removeJob(Connection conn, SchedulingContext ctxt, String jobName, String groupName, boolean activeDeleteSafe) throws JobPersistenceException {
    try {
        Key[] jobTriggers = getDelegate().selectTriggerNamesForJob(conn, jobName, groupName);
        for (int i = 0; i < jobTriggers.length; ++i) {
            deleteTriggerAndChildren(conn, jobTriggers[i].getName(), jobTriggers[i].getGroup());
        }
        return deleteJobAndChildren(conn, ctxt, jobName, groupName);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't remove job: " + e.getMessage(), e);
    }
}
