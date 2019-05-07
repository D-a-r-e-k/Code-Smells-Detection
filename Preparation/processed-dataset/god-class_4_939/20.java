public void addTempAdminhost(User nu) {
    InetAddress ia = nu.conn.clientAddress;
    if (ia != null && mayTempAdminhost(nu.conn.clientIp)) {
        if (!adminHosts.contains(ia)) {
            nu.setAsTempadminhost();
            StringBuffer sb = new StringBuffer("[").append(nu.getName()).append("] ");
            sb.append(nu.conn.clientIp).append(" add tempAdminhost");
            Server.log(this, sb.toString(), MSG_STATE, LVL_MAJOR);
            adminHosts.add(ia);
            if (!tempAdmins.contains(nu.getName().toLowerCase()))
                tempAdmins.add(nu.getName().toLowerCase());
        }
    }
}
