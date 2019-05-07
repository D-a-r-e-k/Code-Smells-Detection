/**
     * The derived class must implement this method and commit all the connections
     * used in this transaction. If the transaction could not commit fully or
     * partially, this method will throw an {@link TransactionAbortedException},
     * causing a rollback to occur as the next step.
     * 
     * @throws TransactionAbortedException The transaction could not commit fully
     *         or partially and should be rolled back.
     */
protected abstract void commitConnections() throws TransactionAbortedException;
