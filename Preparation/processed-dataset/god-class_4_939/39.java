public void storeUser(int action, User u, String message, long millis, String storedBy) {
    if (u == null)
        return;
    if (action != IActionStates.PUNISH && action != IActionStates.FLOCKCOL && action != IActionStates.FLOCKAWAY && action != IActionStates.FLOCKME && action != IActionStates.SUBAN && action != IActionStates.ISPUNISHABLE)
        return;
    if (action == IActionStates.PUNISH && srv.PUNISH_DURATION == -1)
        return;
    if (action == IActionStates.ISPUNISHABLE && (srv.PUNISH_DURATION == -1 || MESSAGE_FLOOD_LENGHT == -1))
        return;
    if ((action == IActionStates.FLOCKCOL || action == IActionStates.FLOCKAWAY || action == IActionStates.FLOCKME) && srv.MAX_FLOCK_DURATION == -1) {
        return;
    }
    String room = "";
    if (UserManager.mgr.getUserByName(storedBy) != null)
        room = UserManager.mgr.getUserByName(storedBy).getGroup().getName();
    String actionString = null;
    if (action == IActionStates.FLOCKCOL)
        actionString = "FLOCKCOL";
    if (action == IActionStates.FLOCKAWAY)
        actionString = "FLOCKAWAY";
    if (action == IActionStates.FLOCKME)
        actionString = "FLOCKME";
    if (action == IActionStates.ISPUNISHABLE)
        actionString = "ISPUNISHABLE";
    if (action == IActionStates.PUNISH)
        actionString = "PUNISH";
    if (action == IActionStates.SUBAN)
        actionString = "SUBAN";
    StringBuffer sb = new StringBuffer("StorUser: User=");
    sb.append(u.getName());
    sb.append(" StoredBy=");
    sb.append(storedBy);
    sb.append(" Action=");
    sb.append(actionString);
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
    sb.append(" room=");
    sb.append(room);
    Server.log(this, sb.toString(), Server.MSG_STATE, Server.LVL_MAJOR);
    ActionstoreObject po = new ActionstoreObject(action, message, storedBy, room, System.currentTimeMillis() + millis);
    if (USE_IP_BAN && u.conn != null && u.conn.isBanable() && po.equalsActionState(IActionStates.ISPUNISHABLE)) {
        po.con = u.conn;
    }
    po.u = u;
    po.cookie = u.getCookie();
    po.usr = u.getName().toLowerCase().trim();
    storeList.put(po, po.usr);
}
