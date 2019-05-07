private void displayUser(User u, StringBuffer sb) {
    sb.append("\r\n<tr>");
    sb.append("\r\n<td class=logout><a href=\"/admin?do=removeuser&name=");
    sb.append(u.getName().toLowerCase());
    sb.append("&cookie=");
    sb.append(u.getCookie());
    sb.append("\">logout</a>");
    sb.append("\r\n</td>");
    sb.append("\r\n<td class=remove>");
    sb.append("\r\n<a href=\"/admin?do=removeuser&name=");
    sb.append(u.getName().toLowerCase());
    sb.append("&cookie=");
    sb.append(u.getCookie());
    sb.append("&force=true\">remove</a>");
    sb.append("</td>");
    sb.append("\r\n<td class=username>");
    sb.append(u.getName());
    sb.append("\r\n</td>");
    sb.append(has_Right(u, IUserStates.ROLE_USER, "user"));
    sb.append(has_Right(u, IUserStates.ROLE_VIP, "vip"));
    sb.append(has_Right(u, IUserStates.ROLE_GOD, "admin"));
    sb.append(has_Right(u, IUserStates.IS_MODERATOR, "moderator"));
    sb.append(has_Right(u, IUserStates.IS_GUEST, "guest"));
    sb.append(has_RightAsshole(u, IUserStates.ROLE_ASSHOLE));
    if (Server.srv.USE_SMILEY)
        sb.append(may_use_Smiley(u));
    sb.append(may_set_Theme(u));
    sb.append(may_call_Memberships(u));
    sb.append("\r\n<td class=name>");
    sb.append("is&nbsp;punished");
    sb.append("\r\n<td class=param>");
    if (u.isPunished()) {
        sb.append("\r\n<a href=\"/ADMIN?do=unpunish&name=").append(u.getName().toLowerCase()).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"remove\"></a>");
    } else {
        sb.append("<img src =\"../static/no.gif\">");
    }
    sb.append("\r\n</td>");
    sb.append("\r\n<td class=name>");
    sb.append("is&nbsp;reg");
    sb.append("\r\n<td class=param>");
    if (!u.isUnregistered) {
        sb.append("<img src =\"../static/ok.gif\">");
    } else {
        sb.append("<img src =\"../static/no.gif\">");
    }
    sb.append("\r\n</td>");
    sb.append("\r\n<td class=groupname>in&nbsp;");
    Group g = u.getGroup();
    sb.append(g != null ? g.getRawName() : "<i>currently changing group</i>");
    sb.append("\r\n</td>");
    sb.append("\r\n</tr>\r\n");
}
