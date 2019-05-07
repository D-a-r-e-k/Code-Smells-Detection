public Set getPausedTriggerGroups(final SchedulingContext ctxt) throws JobPersistenceException {
    return (Set) executeWithoutLock(// no locks necessary for read... 
    new TransactionCallback() {

        public Object execute(Connection conn) throws JobPersistenceException {
            return getPausedTriggerGroups(conn, ctxt);
        }
    });
}
