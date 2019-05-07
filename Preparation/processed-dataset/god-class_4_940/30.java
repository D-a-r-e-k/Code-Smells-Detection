private String may_set_Theme(User u) {
    StringBuffer sb = new StringBuffer("\r\n<td class=name>");
    sb.append("\r\nSet Theme");
    sb.append("\r\n<td class=param>");
    if (u.hasRight(IUserStates.MAY_SET_THEME)) {
        sb.append("\r\n<img src =\"../static/ok.gif\">");
    } else {
        sb.append("\r\n<img src =\"../static/no.gif\">");
    }
    sb.append("\r\n</td>");
    return sb.toString();
}
