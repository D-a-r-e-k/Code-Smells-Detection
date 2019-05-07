public void banConn(Connection conn, String user, String cookie, String message, long millis) {
    if (conn != null && conn.isBanable()) {
        BanObject bo = new BanObject(message, "SYS", System.currentTimeMillis() + millis);
        bo.cookie = cookie;
        bo.usr = user;
        bo.con = conn;
        banList.put(bo.usr, bo);
        banList.put(bo.cookie, bo);
        banList.put(bo.con.getBanKey(), bo);
        StringBuffer sb = new StringBuffer("banUser: User=");
        sb.append(user);
        if (conn == null) {
            sb.append(" Connection-Object was null");
        } else if (conn.clientIp != null) {
            sb.append(" +IP=");
            sb.append(conn.clientIp);
        } else {
            sb.append(" Came over Proxy (Proxy: ");
            sb.append(conn.peerIp);
            sb.append(", ForwardChain: ");
            sb.append(conn.fwChain);
        }
        sb.append(" Duration=");
        sb.append(millis / 1000);
        sb.append("secs Message=");
        sb.append(message);
        Server.log(this, sb.toString(), Server.MSG_AUTH, Server.LVL_MAJOR);
    }
}
