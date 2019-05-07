public void removeTempAdminhost(User nu) {
    if (!tempAdmins.contains(nu.getName().toLowerCase()))
        return;
    InetAddress ia = nu.conn.clientAddress;
    if (ia != null) {
        StringBuffer sb = new StringBuffer("[").append(nu.getName()).append("] ");
        sb.append(nu.conn.clientIp).append(" remove tempAdminhost");
        Server.log(this, sb.toString(), MSG_STATE, LVL_MAJOR);
        adminHosts.remove(ia);
        tempAdmins.remove(nu.getName().toLowerCase());
    }
}
