private String has_Right(User u, int right, String rname) {
    String displayName = null;
    if (rname.equals("moderator")) {
        displayName = "mod";
    } else
        displayName = rname;
    StringBuffer sb = new StringBuffer("\r\n<td class=name>");
    sb.append(displayName.toUpperCase());
    sb.append("\r\n<td class=param>");
    if (u.hasDefaultRight(right)) {
        sb.append("\r\n<img src =\"../static/ok.gif\">");
    } else {
        sb.append("\r\n<img src =\"../static/no.gif\">");
    }
    sb.append("\r\n</td><td class=param>");
    if (u.hasRight(right)) {
        sb.append("\r\n<a href=\"/ADMIN?do=removeright&right=").append(rname).append("&name=").append(u.getName().toLowerCase()).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"remove\"></a>");
    } else {
        sb.append("\r\n<a href=\"/ADMIN?do=giveright&right=").append(rname).append("&name=").append(u.getName().toLowerCase()).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"give\"></a>");
    }
    sb.append("\r\n</td>");
    return sb.toString();
}
