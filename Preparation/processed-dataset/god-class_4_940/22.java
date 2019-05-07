private void displayGroup(Group g, StringBuffer sb) {
    if (g == null) {
        sb.append("group not found");
        return;
    }
    String groupname = g.getRawName().toLowerCase();
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Groupname");
    sb.append("</td>");
    sb.append("<td width=200px class=param>");
    sb.append(groupname);
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Entrace");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.ENTRANCE))
        sb.append("<img src =\"../static/ok.gif\">");
    else
        sb.append("<img src =\"../static/no.gif\">");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Open");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.OPEN))
        sb.append("<img src =\"../static/ok.gif\">");
    else
        sb.append("<img src =\"../static/no.gif\">");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Lockprotected");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.LOCKPROTECTED))
        sb.append("<a href=\"/ADMIN?do=changestate&state=lockprotected&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=lockprotected&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Allow&nbsp;SU");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.ALLOW_SU))
        sb.append("<a href=\"/ADMIN?do=changestate&state=allowsu&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=allowsu&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Minright&nbsp;SU");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasMinRightSu(IUserStates.ROLE_USER))
        sb.append("<a href=\"/ADMIN?do=changestate&state=minuserrolesu&group=").append(groupname).append("&right=vip\">").append("USER").append("</a>");
    else if (g.hasMinRightSu(IUserStates.ROLE_VIP))
        sb.append("<a href=\"/ADMIN?do=changestate&state=minuserrolesu&group=").append(groupname).append("&right=admin\">").append("VIP").append("</a>");
    else if (g.hasMinRightSu(IUserStates.ROLE_GOD))
        sb.append("<a href=\"/ADMIN?do=changestate&state=minuserrolesu&group=").append(groupname).append("&right=moderator\">").append("ADMIN").append("</a>");
    else if (g.hasMinRightSu(IUserStates.IS_MODERATOR))
        sb.append("<a href=\"/ADMIN?do=changestate&state=minuserrolesu&group=").append(groupname).append("&right=user\">").append("MOD").append("</a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Minright&nbsp;Open");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasMinRight(IUserStates.ROLE_USER) || g.hasMinRight(IUserStates.MAY_JOIN_GROUP))
        sb.append("USER");
    else if (g.hasMinRight(IUserStates.ROLE_VIP))
        sb.append("VIP");
    else if (g.hasMinRight(IUserStates.ROLE_GOD))
        sb.append("ADMIN");
    else if (g.hasMinRight(IUserStates.IS_MODERATOR))
        sb.append("MOD");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Membership&nbsp;Open");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.getMembershipRoom() != null)
        sb.append(g.getMembershipRoom());
    else
        sb.append("<img src =\"../static/no.gif\" border =\"0\">");
    sb.append("</td>");
    sb.append("\r\n<td class=name>");
    sb.append("Membershiplock");
    sb.append("\r\n</td>");
    sb.append("\r\n<td class=param>");
    if (g.hasState(IGroupState.JOIN_MEMBERSHIP_LOCKED)) {
        sb.append("\r\n<a href=\"/ADMIN?do=changestate&state=joinmembershiplocked&group=");
        sb.append(groupname);
        sb.append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    } else {
        sb.append("\r\n<a href=\"/ADMIN?do=changestate&state=joinmembershiplocked&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    }
    sb.append("\r\n</td>");
    sb.append("<tr>");
    sb.append("<td class=name colspan=2>");
    sb.append("<a href=\"/ADMIN?do=refreshgp&&group=").append(groupname).append("\"><img src =\"../static/refresh.gif\" border =\"0\" alt=\"refresh Grouppolice\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("BBC");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.ALLOW_USE_BBCODES))
        sb.append("<a href=\"/ADMIN?do=changestate&state=allowusebbcodes&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=allowusebbcodes&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Hitdice");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (!g.hasState(IGroupState.DEACTIVATE_HITDICE))
        sb.append("<a href=\"/ADMIN?do=changestate&state=hitdice&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=hitdice&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("No&nbsp;SU first");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.NO_SU_FIRST))
        sb.append("<a href=\"/ADMIN?do=changestate&state=nosufirst&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=nosufirst&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("SU&nbsp;setTheme");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.SU_CAN_SETTHEME))
        sb.append("<a href=\"/ADMIN?do=changestate&state=sucansettheme&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=sucansettheme&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("SU&nbsp;Ban");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.SU_CAN_BAN))
        sb.append("<a href=\"/ADMIN?do=changestate&state=sucanban&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=sucanban&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("AutoSuList");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.getAutoSuList() != null && g.getAutoSuList().size() > 0) {
        for (Enumeration<String> e = g.getAutoSuList().elements(); e.hasMoreElements(); ) {
            sb.append((String) e.nextElement());
            if (e.hasMoreElements()) {
                sb.append("<b>,</b>");
            }
        }
    } else
        sb.append("<img src =\"../static/no.gif\" border =\"0\">");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("AutoSuMembershipList");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.getAutoSuMembership() != null && g.getAutoSuMembership().length > 0) {
        Membership[] values = g.getAutoSuMembership();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i].key);
            if (i < values.length - 1) {
                sb.append("<b>,</b>");
            }
        }
    } else
        sb.append("<img src =\"../static/no.gif\" border =\"0\">");
    sb.append("<td class=name>");
    sb.append("MemberRoom");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.getMemberships() != null && g.getMemberships().length > 0) {
        Membership[] values = g.getMemberships();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i].key);
            if (i < values.length - 1) {
                sb.append("<b>,</b>");
            }
        }
    } else
        sb.append("<img src =\"../static/no.gif\" border =\"0\">");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("\r\n<tr>");
    sb.append("\r\n<td class=name colspan=2>");
    sb.append("\r\n<img title=\"opener\" src=\"../static/schluessel.gif\">  ");
    User u = UserManager.mgr.getUserByName(g.getOpener());
    if (u != null) {
        if (Server.srv.USE_FADECOLOR) {
            if (u.getFadeColCode() != null && u.getFadeColorUsername() != null) {
                sb.append(u.getFadeColorUsername().toString());
            } else
                sb.append(u.getNoFadeColorUsername());
        } else
            sb.append(EntityDecoder.charToHtml(u.getName()));
    } else {
        sb.append("<i>").append(g.getOpener()).append("</i>");
    }
    sb.append("\r\n</td>");
    sb.append("<td class=name>");
    sb.append("Punishable");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.CAN_SET_PUNISHABLE))
        sb.append("<a href=\"/ADMIN?do=changestate&state=cansetpunishable&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=cansetpunishable&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    sb.append("<td class=name>");
    sb.append("Moderated");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (g.hasState(IGroupState.MODERATED))
        sb.append("<a href=\"/ADMIN?do=changestate&state=moderated&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    else
        sb.append("<a href=\"/ADMIN?do=changestate&state=moderated&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    sb.append("</td>");
    if (Server.srv.USE_SMILEY) {
        sb.append("\r\n<td class=name>");
        sb.append("Smiley");
        sb.append("\r\n</td>");
        sb.append("\r\n<td class=param>");
        if (g.hasState(IGroupState.ALLOW_USE_SMILEY)) {
            sb.append("<a href=\"/ADMIN?do=changestate&state=allowusesmiley&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
        } else {
            sb.append("\r\n<a href=\"/ADMIN?do=changestate&state=allowusesmiley&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
        }
        sb.append("\r\n</td>");
    }
    sb.append("<td class=name>");
    sb.append("Mod&nbsp;Timelock");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(g.getTimelockSec());
    sb.append("&nbsp;sec<br>");
    sb.append("<a style=\"text-decoration:none\" href=\"/ADMIN?do=changestate&state=timelocksub&group=").append(groupname).append("\">-</a>");
    sb.append("<b>/</b>");
    sb.append("<a style=\"text-decoration:none\" href=\"/ADMIN?do=changestate&state=timelockadd&group=").append(groupname).append("\">+</a>");
    sb.append("\r\n</td>");
    sb.append("\r\n<td class=name>");
    sb.append("join Unreg");
    sb.append("\r\n</td>");
    sb.append("\r\n<td class=param>");
    if (g.hasState(IGroupState.NOT_ALLOW_JOIN_UNREG)) {
        sb.append("\r\n<a href=\"/ADMIN?do=changestate&state=joinunreg&group=").append(groupname).append("\"><img src =\"../static/no.gif\" border =\"0\" alt=\"change\"></a>");
    } else {
        sb.append("\r\n<a href=\"/ADMIN?do=changestate&state=joinunreg&group=").append(groupname).append("\"><img src =\"../static/ok.gif\" border =\"0\" alt=\"change\"></a>");
    }
    sb.append("\r\n</td>");
    sb.append("\r\n</tr>\r\n");
}
