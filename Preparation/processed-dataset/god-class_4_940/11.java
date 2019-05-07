private void giveRight(IRequest req, StringBuffer sb) {
    String usrName = req.getValue("name");
    String right = req.getValue("right");
    User ur = UserManager.mgr.getUserByName(usrName);
    if (ur != null) {
        if (right.equals("user")) {
            ur.setNewPermission(IUserStates.ROLE_USER);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        } else if (right.equals("vip")) {
            ur.setNewPermission(IUserStates.ROLE_VIP);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        } else if (right.equals("admin")) {
            if (slevel() == 1)
                ur.setNewPermission(IUserStates.ROLE_GOD);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        } else if (right.equals("moderator")) {
            ur.givePermission(IUserStates.IS_MODERATOR);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        } else if (right.equals("guest")) {
            ur.givePermission(IUserStates.IS_GUEST);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        }
        sb.append("</table>");
    } else {
        sb.append("<table class=mainTable>");
        sb.append("<tr>");
        sb.append("<td class=name >");
        sb.append("user not found");
        sb.append("</td></tr>");
        sb.append("</table>");
    }
}
