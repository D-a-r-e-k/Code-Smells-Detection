private void unpunishUser(IRequest req, StringBuffer sb) {
    String usrName = req.getValue("name");
    User ur = UserManager.mgr.getUserByName(usrName);
    if (ur != null) {
        ur.setPunish(false);
        sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
        displayUser(ur, sb);
    } else {
        sb.append("<table class=mainTable>");
        sb.append("<tr>");
        sb.append("<td class=name >");
        sb.append("user not found");
        sb.append("</td></tr>");
    }
}
