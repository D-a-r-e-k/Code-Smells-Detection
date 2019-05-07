private void removeBan(IRequest req, StringBuffer sb) {
    String val = req.getValue("name");
    if (val != null) {
        if (Server.srv.removeBan(val))
            sb.append("removed ban for user " + val);
        else
            sb.append("No ban found for user " + val);
    } else {
        val = req.getValue("host");
        if (Server.srv.removeBan(val))
            sb.append("removed ban for host " + val);
        else
            sb.append("No ban found for host " + val);
    }
}
