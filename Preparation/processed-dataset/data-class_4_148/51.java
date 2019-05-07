protected void releaseLock(Connection conn, String lockName, boolean doIt) {
    if (doIt && conn != null) {
        try {
            getLockHandler().releaseLock(conn, lockName);
        } catch (LockException le) {
            getLog().error("Error returning lock: " + le.getMessage(), le);
        }
    }
}
