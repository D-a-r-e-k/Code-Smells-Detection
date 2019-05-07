public void permaBanHost(InetAddress ia, String msg) {
    BanObject bo = new BanObject(msg, "Config( parmaBannedIp )", 0);
    bo.hostban = ia.getHostAddress();
    banList.put(bo.hostban, bo);
    StringBuffer sb = new StringBuffer("banHost: Host=");
    sb.append(bo.hostban);
    if (ia.getHostName() != null) {
        sb.append(" (");
        sb.append(ia.getHostName());
        sb.append(")");
    }
    sb.append(" Message=");
    sb.append(msg);
    Server.log(this, sb.toString(), Server.MSG_STATE, Server.LVL_MAJOR);
}
