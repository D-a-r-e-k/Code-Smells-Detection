public boolean isTrafficBanned(Object o) {
    // null is not ban-able  
    if (o == null)
        return false;
    // get the ban-object if there is one  
    BanObject b = null;
    if (o instanceof Connection)
        return false;
    InetAddress ia = null;
    try {
        ia = (InetAddress) o;
    } catch (ClassCastException ce) {
        Server.log(this, ce.toString(), Server.MSG_ERROR, Server.LVL_MAJOR);
        return false;
    }
    if (o instanceof InetAddress) {
        b = (BanObject) banList.get(ia.getHostAddress());
    }
    // not-banned if there is no ban-object  
    if (b == null || b.hostban == null)
        return false;
    if (b.bannedBy.equals("Config( parmaBannedIp )"))
        return true;
    if (b.hostban != null && !b.hostban.equals(ia.getHostAddress()))
        return false;
    // check if the ban is still fresh  
    if (b.time < System.currentTimeMillis()) {
        banList.remove(o);
        return false;
    }
    return true;
}
