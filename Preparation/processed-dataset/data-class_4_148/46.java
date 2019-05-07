/**
     * <p>
     * Called by the QuartzScheduler to inform the <code>JobStore</code> that
     * it should free up all of it's resources because the scheduler is
     * shutting down.
     * </p>
     */
public void shutdown() {
    if (clusterManagementThread != null) {
        clusterManagementThread.shutdown();
    }
    if (misfireHandler != null) {
        misfireHandler.shutdown();
    }
    try {
        DBConnectionManager.getInstance().shutdown(getDataSource());
    } catch (SQLException sqle) {
        getLog().warn("Database connection shutdown unsuccessful.", sqle);
    }
}
