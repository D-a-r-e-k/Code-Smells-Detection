private void renderGrouplist(IRequest req, StringBuffer sb) {
    Group[] grps = GroupManager.mgr.currentGroupList();
    sb.append("<b>FreeCS Grouplist</b><br>");
    sb.append("<table class=mainTable>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("open group(s): ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(GroupManager.mgr.openGroupsCount());
    sb.append("</td>");
    sb.append("</tr>");
    for (int i = 0; i < grps.length; i++) {
        Group g = grps[i];
        displayGroup(g, sb);
    }
    sb.append("</table");
}
