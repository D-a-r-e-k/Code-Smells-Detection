private String may_use_Smiley(User u) {
    StringBuffer sb = new StringBuffer("\r\n<td class=name>");
    sb.append("\r\nSM");
    sb.append("\r\n<td class=param>");
    if (u.hasRight(IUserStates.MAY_USE_SMILEY)) {
        sb.append("\r\n<img src =\"../static/ok.gif\">");
    } else {
        sb.append("\r\n<img src =\"../static/no.gif\">");
    }
    sb.append("\r\n</td>");
    return sb.toString();
}
