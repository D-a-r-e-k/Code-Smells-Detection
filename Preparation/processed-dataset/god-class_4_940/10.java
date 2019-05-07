private void removeRight(IRequest req, StringBuffer sb) {
    String usrName = req.getValue("name");
    String right = req.getValue("right");
    User ur = UserManager.mgr.getUserByName(usrName);
    if (ur != null) {
        if (right.equals("user")) {
            ur.setNewPermission(IUserStates.ROLE_ASSHOLE);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        } else if (right.equals("vip")) {
            ur.setNewPermission(IUserStates.ROLE_USER);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        } else if (right.equals("admin")) {
            ur.setNewPermission(IUserStates.ROLE_USER);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        } else if (right.equals("moderator")) {
            ur.takePermission(IUserStates.IS_MODERATOR);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        } else if (right.equals("guest")) {
            ur.takePermission(IUserStates.IS_GUEST);
            sb.append("<b>FreeCS-Userlist</b><br><table class=mainTable>");
            displayUser(ur, sb);
        }
        sb.append("</table>");
    } else {
        sb.append("<table class=mainTable>");
        sb.append("<tr>");
        sb.append("<td class=name");
        sb.append("user not found");
        sb.append("</td></tr>");
        sb.append("</table>");
    }
}
