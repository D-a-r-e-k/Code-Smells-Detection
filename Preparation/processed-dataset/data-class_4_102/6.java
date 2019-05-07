/**
     * Clears all parameters given to the PreparedStatements and all their warnings. 
     * Afterwards this PoolElement is marked as inactive (isActive = false)
     */
public void release() {
    try {
        if (select != null) {
            select.clearParameters();
            select.clearWarnings();
        }
        if (update != null) {
            update.clearParameters();
            update.clearWarnings();
        }
        if (insert != null) {
            insert.clearParameters();
            insert.clearWarnings();
        }
    } catch (Exception se) {
        Server.debug(this, "catched exception while releasing PoolElement", se, Server.MSG_AUTH, Server.LVL_MAJOR);
    }
    this.isActive = false;
}
