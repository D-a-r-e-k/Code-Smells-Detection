/**
     * Checks the given Statement for SQLWarnings and logs them.
     * @param s The statement to check for Warnings
     */
public void checkWarnings(Statement s, String prefix) {
    try {
        SQLWarning sqlW = s.getWarnings();
        while (sqlW != null) {
            StringBuffer sb = new StringBuffer(this.toString());
            sb.append(" getResultSet: Encountered SQLWarning: ");
            sb.append(prefix);
            sb.append(": ");
            sb.append(sqlW.getErrorCode());
            sb.append(": ");
            sb.append(sqlW.getCause());
            Server.log(Thread.currentThread(), sb.toString(), Server.MSG_ERROR, Server.LVL_MAJOR);
            sqlW = sqlW.getNextWarning();
        }
    } catch (SQLException se) {
        this.isValid = false;
        Server.debug(this, "checkWarnings caused exception", se, Server.MSG_ERROR, Server.LVL_MAJOR);
    }
}
