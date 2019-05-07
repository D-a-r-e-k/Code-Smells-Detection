/**
     * The derived class must implement this method and rollback all the
     * connections used in this transaction. The connections may be closed, as
     * they will not be reused in this transaction. This operation is guaranteed
     * to succeed.
     */
protected abstract void rollbackConnections();
