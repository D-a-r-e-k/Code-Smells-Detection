private void removeUser(IRequest req, StringBuffer sb) {
    String usrName = req.getValue("name");
    String usrCookie = req.getValue("cookie");
    String force = req.getValue("force");
    User ur = UserManager.mgr.getUserByCookie(usrCookie);
    if (ur == null)
        ur = UserManager.mgr.getUserByName(usrName);
    if (ur != null) {
        ur.sendQuitMessage(false, null);
        if ("true".equalsIgnoreCase(force)) {
            UserManager.mgr.ustr.removeUser(ur);
            sb.append("forcibly removed user");
        } else {
            sb.append("removed user");
        }
    } else {
        sb.append("user not found");
    }
}
