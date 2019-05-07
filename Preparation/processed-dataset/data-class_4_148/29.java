/**
     * Set the transaction isolation level of DB connections to sequential.
     * 
     * @param b
     */
public void setTxIsolationLevelSerializable(boolean b) {
    setTxIsolationLevelSequential = b;
}
