private void changeState(IRequest req, StringBuffer sb) {
    String group = req.getValue("group");
    String state = req.getValue("state");
    String right = req.getValue("right");
    Group g = GroupManager.mgr.getGroup(group);
    if (g != null) {
        if (state.equals("timelocksub")) {
            int t_old = g.getTimelockSec();
            g.setTimelockSec(t_old - 5);
        } else if (state.equals("timelockadd")) {
            int t_old = g.getTimelockSec();
            g.setTimelockSec(t_old + 5);
        } else if (state.equals("allowusebbcodes")) {
            if (g.hasState(IGroupState.ALLOW_USE_BBCODES)) {
                g.unsetState(IGroupState.ALLOW_USE_BBCODES);
            } else
                g.setState(IGroupState.ALLOW_USE_BBCODES);
        } else if (state.equals("allowusesmiley")) {
            if (g.hasState(IGroupState.ALLOW_USE_SMILEY)) {
                g.unsetState(IGroupState.ALLOW_USE_SMILEY);
            } else {
                g.setState(IGroupState.ALLOW_USE_SMILEY);
            }
        } else if (state.equals("lockprotected")) {
            if (g.hasState(IGroupState.LOCKPROTECTED)) {
                g.unsetState(IGroupState.LOCKPROTECTED);
            } else {
                g.setState(IGroupState.OPEN);
                g.setState(IGroupState.LOCKPROTECTED);
            }
        } else if (state.equals("allowsu")) {
            if (g.hasState(IGroupState.ALLOW_SU)) {
                g.unsetState(IGroupState.ALLOW_SU);
            } else
                g.setState(IGroupState.ALLOW_SU);
        } else if (state.equals("minuserrolesu")) {
            if (right.equals("user")) {
                g.setMinRightSu(IUserStates.ROLE_USER);
            } else if (right.equals("vip")) {
                g.setMinRightSu(IUserStates.ROLE_VIP);
            } else if (right.equals("moderator")) {
                g.setMinRightSu(IUserStates.IS_MODERATOR);
            } else if (right.equals("admin")) {
                g.setMinRightSu(IUserStates.ROLE_GOD);
            }
        } else if (state.equals("moderated")) {
            if (g.hasState(IGroupState.MODERATED)) {
                g.unsetState(IGroupState.MODERATED);
            } else
                g.setState(IGroupState.MODERATED);
        } else if (state.equals("hitdice")) {
            if (g.hasState(IGroupState.DEACTIVATE_HITDICE)) {
                g.unsetState(IGroupState.DEACTIVATE_HITDICE);
            } else
                g.setState(IGroupState.DEACTIVATE_HITDICE);
        } else if (state.equals("nosufirst")) {
            if (g.hasState(IGroupState.NO_SU_FIRST)) {
                g.unsetState(IGroupState.NO_SU_FIRST);
            } else
                g.setState(IGroupState.NO_SU_FIRST);
        } else if (state.equals("sucanban")) {
            if (g.hasState(IGroupState.SU_CAN_BAN)) {
                g.unsetState(IGroupState.SU_CAN_BAN);
            } else
                g.setState(IGroupState.SU_CAN_BAN);
        } else if (state.equals("sucansettheme")) {
            if (g.hasState(IGroupState.SU_CAN_SETTHEME)) {
                g.unsetState(IGroupState.SU_CAN_SETTHEME);
            } else
                g.setState(IGroupState.SU_CAN_SETTHEME);
        } else if (state.equals("cansetpunishable")) {
            if (g.hasState(IGroupState.CAN_SET_PUNISHABLE)) {
                g.unsetState(IGroupState.CAN_SET_PUNISHABLE);
            } else
                g.setState(IGroupState.CAN_SET_PUNISHABLE);
        } else if (state.equals("joinunreg")) {
            if (g.hasState(IGroupState.NOT_ALLOW_JOIN_UNREG)) {
                g.unsetState(IGroupState.NOT_ALLOW_JOIN_UNREG);
            } else {
                g.setState(IGroupState.NOT_ALLOW_JOIN_UNREG);
            }
        } else if (state.equals("joinmembershiplocked")) {
            if (g.hasState(IGroupState.JOIN_MEMBERSHIP_LOCKED)) {
                g.unsetState(IGroupState.JOIN_MEMBERSHIP_LOCKED);
            } else {
                g.setState(IGroupState.JOIN_MEMBERSHIP_LOCKED);
            }
        }
        sb.append("<b>FreeCS-Grouplist</b><br /><table class=mainTable>");
        displayGroup(g, sb);
        sb.append("</table>");
    } else {
        sb.append("<b>FreeCS-Grouplist</b><br /><table class=mainTable>");
        sb.append("<tr>");
        sb.append("<td class=name");
        sb.append("group not found");
        sb.append("</td></tr>");
        sb.append("</table>");
    }
}
