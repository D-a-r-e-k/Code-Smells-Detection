private void refreshGp(IRequest req, StringBuffer sb) {
    String group = req.getValue("group");
    Group g = GroupManager.mgr.getGroup(group);
    StringBuffer file = new StringBuffer(Server.BASE_PATH);
    file.append("/grouppolicies/");
    file.append(group.toLowerCase());
    file.append(".properties");
    File f = new File(file.toString());
    if (f.exists()) {
        g = checkProperties(f, g);
        sb.append("<b>FreeCS-Grouplist</b><br><table class=mainTable>");
        displayGroup(g, sb);
        sb.append("</table>");
    } else {
        sb.append("<b>FreeCS-Grouplist</b><br><table class=mainTable>");
        displayGroup(g, sb);
        sb.append("<tr>");
        sb.append("<td class=name>");
        sb.append("File:").append(file).append("&nbsp;not&nbsp;found");
        sb.append("</td></tr>");
        sb.append("</table>");
    }
}
