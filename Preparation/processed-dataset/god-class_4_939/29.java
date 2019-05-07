public void banUser(User u, String message, long millis, String bannedBy) {
    if (u == null)
        return;
    StringBuffer sb = new StringBuffer("banUser: User=");
    sb.append(u.getName());
    sb.append(" BannedBy=");
    sb.append(bannedBy);
    sb.append(" Cookie=");
    sb.append(u.getCookie());
    if (u.conn == null) {
        sb.append(" Connection-Object was null");
    } else if (u.conn.clientIp != null) {
        sb.append(" +IP=");
        sb.append(u.conn.clientIp);
    } else {
        sb.append(" Came over Proxy (Proxy: ");
        sb.append(u.conn.peerIp);
        sb.append(", ForwardChain: ");
        sb.append(u.conn.fwChain);
    }
    sb.append(" Duration=");
    sb.append(millis / 1000);
    sb.append("secs Message=");
    sb.append(message);
    Server.log(this, sb.toString(), Server.MSG_AUTH, Server.LVL_MAJOR);
    BanObject bo = new BanObject(message, bannedBy, System.currentTimeMillis() + millis);
    bo.cookie = u.getCookie();
    bo.usr = u.getName().toLowerCase().trim();
    bo.email = (String) u.getProperty("email");
    if (bo.email != null) {
        bo.email = bo.email.trim().toLowerCase();
        banList.put(bo.email, bo);
    }
    banList.put(bo.usr, bo);
    banList.put(bo.cookie, bo);
    if (USE_IP_BAN && u.conn != null && u.conn.isBanable()) {
        bo.con = u.conn;
        banList.put(bo.con.getBanKey(), bo);
    }
    u.sendQuitMessage(true, null);
}
