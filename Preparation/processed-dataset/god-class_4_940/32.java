private String may_call_Memberships(User u) {
    StringBuffer sb = new StringBuffer("\r\n<td class=name>");
    sb.append("\r\nMcall");
    sb.append("\r\n<td class=param>");
    if (u.hasRight(IUserStates.MAY_CALL_MEMBERSHIPS)) {
        sb.append("\r\n<img src =\"../static/ok.gif\">");
    } else {
        sb.append("\r\n<img src =\"../static/no.gif\">");
    }
    sb.append("\r\n</td>");
    return sb.toString();
}
