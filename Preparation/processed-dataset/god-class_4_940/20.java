private void searchUser(IRequest req, StringBuffer sb) {
    String usrName = req.getValue("usr");
    User u = null;
    sb.append("<b>FreeCS-Usersearch</b><br><table class=mainTable>");
    if (usrName == null) {
        sb.append("<br>missing Username");
    } else
        u = UserManager.mgr.getUserByName(usrName);
    if (u != null) {
        displayUser(u, sb);
    } else if (usrName != null) {
        sb.append("<br>User not found");
    }
}
