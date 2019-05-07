private String has_RightAsshole(User u, int right) {
    StringBuffer sb = new StringBuffer("\r\n<td class=name>");
    sb.append("\r\nASSHOLE");
    sb.append("\r\n<td class=param>");
    if (u.hasDefaultRight(right)) {
        sb.append("\r\n<img src =\"../static/ok.gif\">");
    } else {
        sb.append("\r\n<img src =\"../static/no.gif\">");
    }
    sb.append("\r\n</td><td class=param>");
    if (u.hasRight(right)) {
        sb.append("\r\n<img src =\"../static/ok.gif\">");
    } else {
        sb.append("\r\n<img src =\"../static/no.gif\">");
    }
    sb.append("\r\n</td>");
    return sb.toString();
}
