private void searchGroup(IRequest req, StringBuffer sb) {
    String group = req.getValue("group");
    Group g = null;
    sb.append("<b>FreeCS-Groupsearch</b><br><table class=mainTable>");
    if (group == null) {
        sb.append("<br>missing Groupname");
    } else
        g = GroupManager.mgr.getGroup(group);
    if (g != null) {
        displayGroup(g, sb);
    } else if (group != null) {
        sb.append("<br>group (").append(group).append(") not found");
    }
}
