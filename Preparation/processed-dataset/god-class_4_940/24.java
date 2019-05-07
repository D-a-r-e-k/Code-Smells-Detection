private void renderBanlist(IRequest req, StringBuffer sb) {
    BanObject[] bArr = Server.srv.getBanList();
    sb.append("<b>FreeCS-BanList</b><br>");
    if (bArr.length < 1) {
        sb.append("There are no BanObjects at the moment");
    } else {
        StringBuffer ub = new StringBuffer("<table border=0><tr><td class=\"param\"><b>User (ip)</b></td><td class=\"param\"><b>Banned by</b></td><td class=\"param\">Banned until</td><td class=\"param\">Release</td><td class=\"param\">Message</td></tr>");
        StringBuffer hb = new StringBuffer("<table border=0><tr><td class=\"param\"><b>Host</b></td><td class=\"param\"><b>Hostname</b></td><td class=\"param\">Banned until</td><td class=\"param\">Release</td><td class=\"param\">details</td></tr>");
        Vector<BanObject> v = new Vector<BanObject>();
        for (int i = 0; i < bArr.length; i++) {
            BanObject curr = bArr[i];
            if (curr.hostban == null) {
                if (v.contains(curr)) {
                    continue;
                }
                v.add(curr);
                ub.append("<tr><td class=\"param\">");
                ub.append(curr.usr);
                ub.append(" (");
                ub.append(curr.con);
                ub.append(")</td><td class=\"param\">");
                ub.append(curr.bannedBy);
                ub.append("</td><td class=\"param\">");
                ub.append(Server.formatDefaultTimeStamp(curr.time));
                ub.append("</td><td class=\"param\">");
                ub.append("<a href=/admin?do=removeban&name=");
                ub.append(curr.usr);
                ub.append(">X</a></td><td  class=\"param\">");
                ub.append(curr.msg);
                ub.append("</td></tr>");
            } else {
                hb.append("<tr><td class=\"param\">");
                hb.append(curr.hostban);
                hb.append("</td><td class=\"param\">");
                InetAddress ia;
                try {
                    ia = InetAddress.getByName(curr.hostban);
                    hb.append(ia.getHostName());
                } catch (UnknownHostException e) {
                }
                hb.append("</td><td class=\"param\">");
                if (curr.time > 0) {
                    hb.append(Server.formatDefaultTimeStamp(curr.time));
                } else {
                    hb.append("--.--.----");
                }
                hb.append("</td><td class=\"param\">");
                hb.append("<a href=/admin?do=removeban&host=");
                hb.append(curr.hostban);
                hb.append(">X</a></td><td class=\"param\">");
                hb.append(curr.msg);
                hb.append("</td></tr>");
            }
        }
        ub.append("</table>");
        hb.append("</table>");
        sb.append("<table border=0><tr><td valign=top>");
        sb.append(ub.toString());
        sb.append("</td><td width=2 bgcolor=#000000></td><td valign=top>");
        sb.append(hb.toString());
        sb.append("</td></tr></table>");
    }
    sb.append("</table>");
}
