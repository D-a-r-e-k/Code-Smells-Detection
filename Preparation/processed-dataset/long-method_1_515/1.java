/**
	 * causes this PoolElement to close all open cursors and the connection to it's jdbc-source
	 */
public synchronized void cleanup() {
    if (cleanedUp)
        return;
    try {
        if (select != null) {
            select.close();
            select = null;
        }
    } catch (SQLException se) {
        Server.debug(this, "cleanup: select.close()", se, Server.MSG_ERROR, Server.LVL_MAJOR);
    }
    try {
        if (insert != null) {
            insert.close();
            insert = null;
        }
    } catch (SQLException se) {
        Server.debug(this, "cleanup: insert.close()", se, Server.MSG_ERROR, Server.LVL_MAJOR);
    }
    try {
        if (update != null) {
            update.close();
            update = null;
        }
    } catch (SQLException se) {
        Server.debug(this, "cleanup: update.close()", se, Server.MSG_ERROR, Server.LVL_MAJOR);
    }
    try {
        if (con != null) {
            con.close();
            con = null;
        }
    } catch (SQLException se) {
        Server.debug(this, "cleanup: connection.close()", se, Server.MSG_ERROR, Server.LVL_MAJOR);
    }
    this.pool = null;
    this.isActive = false;
    this.cleanedUp = true;
    Server.log("SqlAuthenticator", "Closed Connetion " + this.toString(), Server.MSG_AUTH, Server.LVL_MAJOR);
}
