/**
     * Recover any failed or misfired jobs and clean up the data store as
     * appropriate.
     * 
     * @throws JobPersistenceException if jobs could not be recovered
     */
protected void recoverJobs() throws JobPersistenceException {
    executeInNonManagedTXLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            recoverJobs(conn);
        }
    });
}
