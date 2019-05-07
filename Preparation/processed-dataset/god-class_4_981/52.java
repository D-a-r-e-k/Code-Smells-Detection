/**
     * Removes all volatile data.
     * 
     * @throws JobPersistenceException If jobs could not be recovered.
     */
protected void cleanVolatileTriggerAndJobs() throws JobPersistenceException {
    executeInNonManagedTXLock(LOCK_TRIGGER_ACCESS, new VoidTransactionCallback() {

        public void execute(Connection conn) throws JobPersistenceException {
            cleanVolatileTriggerAndJobs(conn);
        }
    });
}
