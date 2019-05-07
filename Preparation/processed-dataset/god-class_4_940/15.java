private void removeActionstore(IRequest req, StringBuffer sb) {
    String val = req.getValue("name");
    String action = req.getValue("action");
    int reason = new Integer(action).intValue();
    if (val != null) {
        if (Server.srv.removeStore(val, reason))
            sb.append("removed action for user " + val);
        else
            sb.append("No action found for user " + val);
    }
}
