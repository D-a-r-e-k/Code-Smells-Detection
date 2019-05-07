private void doLoginUpdates(User nu, ResultSet rs) throws Exception {
    boolean updated = false, error = false;
    long ts = System.currentTimeMillis();
    int idx = dbp.nameV.indexOf("lastlogin");
    if (idx > -1) {
        try {
            switch(dbp.types[idx]) {
                case Types.INTEGER:
                case Types.SMALLINT:
                    rs.updateInt(idx + 1, (int) (ts / 1000));
                    break;
                case Types.BIGINT:
                case Types.NUMERIC:
                case Types.DECIMAL:
                    rs.updateLong(idx + 1, ts / 1000);
                    break;
                case Types.DATE:
                case Types.TIMESTAMP:
                    rs.updateTimestamp(idx + 1, new Timestamp(ts));
                    break;
                default:
                    rs.updateString(idx + 1, String.valueOf(ts / 1000));
            }
            updated = true;
        } catch (SQLException se) {
            Server.debug(Thread.currentThread(), this.toString() + "LOGIN unable to update lastlogin", se, Server.MSG_AUTH, Server.LVL_MAJOR);
            error = true;
        }
    }
    // update the cookie too (if set in the db properties)  
    idx = dbp.nameV.indexOf("cookie");
    if (idx > -1)
        try {
            rs.updateString(idx + 1, HashUtils.encodeMD5(nu.getCookie()));
        } catch (SQLException se) {
            Server.debug(Thread.currentThread(), this.toString() + "LOGIN unable to update cookie", se, Server.MSG_AUTH, Server.LVL_MAJOR);
        }
    try {
        if (updated) {
            rs.updateRow();
            con.commit();
        } else if (error) {
            rs.cancelRowUpdates();
        }
    } catch (SQLException se) {
        Server.debug(Thread.currentThread(), this.toString() + "LOGIN exception during updateRow/cancelRowUpdates", se, Server.MSG_AUTH, Server.LVL_MAJOR);
    }
}
