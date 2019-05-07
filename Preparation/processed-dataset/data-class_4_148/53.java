/**
     * <p>
     * Removes all volatile data.
     * </p>
     * 
     * @throws JobPersistenceException
     *           if jobs could not be recovered
     */
protected void cleanVolatileTriggerAndJobs(Connection conn) throws JobPersistenceException {
    try {
        // find volatile jobs & triggers... 
        Key[] volatileTriggers = getDelegate().selectVolatileTriggers(conn);
        Key[] volatileJobs = getDelegate().selectVolatileJobs(conn);
        for (int i = 0; i < volatileTriggers.length; i++) {
            removeTrigger(conn, null, volatileTriggers[i].getName(), volatileTriggers[i].getGroup());
        }
        getLog().info("Removed " + volatileTriggers.length + " Volatile Trigger(s).");
        for (int i = 0; i < volatileJobs.length; i++) {
            removeJob(conn, null, volatileJobs[i].getName(), volatileJobs[i].getGroup(), true);
        }
        getLog().info("Removed " + volatileJobs.length + " Volatile Job(s).");
        // clean up any fired trigger entries 
        getDelegate().deleteVolatileFiredTriggers(conn);
    } catch (Exception e) {
        throw new JobPersistenceException("Couldn't clean volatile data: " + e.getMessage(), e);
    }
}
