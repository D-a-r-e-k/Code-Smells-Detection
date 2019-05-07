private void renderActionstorelist(IRequest req, StringBuffer sb) {
    ActionstoreObject[] pArr = Server.srv.getStoreList();
    sb.append("<b>FreeCS-ActionstoreList</b><br>");
    if (pArr.length < 1) {
        sb.append("There are no ActionstoreObjects at the moment");
    } else {
        StringBuffer ub = new StringBuffer();
        StringBuffer hb = new StringBuffer();
        ub.append("<table border=0><tr><td class=\"param\"><b>Action</b></td><td class=\"param\"><b>Room</b></td><td class=\"param\"><b>User </b></td><td class=\"param\"><b>Stored by</b></td><td class=\"param\">Stored until</td><td class=\"param\">Release</td><td class=\"param\">Message</td></tr>");
        for (int i = 0; i < pArr.length; i++) {
            ActionstoreObject curr = pArr[i];
            if (curr.usr != null) {
                ub.append("<tr><td class=\"param\">");
                ub.append(curr.rendererActionState());
                ub.append("</td><td class=\"param\">");
                ub.append(curr.room);
                ub.append("</td><td class=\"param\">");
                ub.append(curr.usr);
                ub.append("</td><td class=\"param\">");
                ub.append(curr.storedBy);
                ub.append("</td><td class=\"param\">");
                ub.append(Server.formatDefaultTimeStamp(curr.time));
                ub.append("</td><td class=\"param\">");
                ub.append("<a href=\"/admin?do=removeaction&name=");
                ub.append(curr.usr);
                ub.append("&action=");
                ub.append(curr.action);
                ub.append("\">X</a></td><td class=\"param\">");
                ub.append(curr.msg);
                ub.append("</td></tr>");
            }
        }
        ub.append("</table>");
        sb.append("<table border=0><tr><td valign=top>");
        sb.append(ub.toString());
        sb.append("</td><td width=2 bgcolor=#000000></td><td valign=top>");
        sb.append(hb.toString());
        sb.append("</td></tr></table>");
    }
    sb.append("</table>");
}
