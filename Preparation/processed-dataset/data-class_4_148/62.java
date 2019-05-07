/**
     * <p>
     * Insert or update a job.
     * </p>
     */
protected void storeJob(Connection conn, SchedulingContext ctxt, JobDetail newJob, boolean replaceExisting) throws ObjectAlreadyExistsException, JobPersistenceException {
    if (newJob.isVolatile() && isClustered()) {
        getLog().info("note: volatile jobs are effectively non-volatile in a clustered environment.");
    }
    boolean existingJob = jobExists(conn, newJob.getName(), newJob.getGroup());
    try {
        if (existingJob) {
            if (!replaceExisting) {
                throw new ObjectAlreadyExistsException(newJob);
            }
            getDelegate().updateJobDetail(conn, newJob);
        } else {
            getDelegate().insertJobDetail(conn, newJob);
        }
    } catch (IOException e) {
        throw new JobPersistenceException("Couldn't store job: " + e.getMessage(), e);
    } catch (SQLException e) {
        throw new JobPersistenceException("Couldn't store job: " + e.getMessage(), e);
    }
}
