/**
     * The derived class must implement this method and close all the connections
     * used in this transaction.
     * 
     * @throws TransactionAbortedException The transaction could not close all the
     *         connections.
     */
protected abstract void closeConnections() throws TransactionAbortedException;
