private void renderConfigoverview(IRequest req, StringBuffer sb) {
    sb.append("<b>FreeCS-Config Overview</b><br>");
    sb.append("<table class=mainTable>");
    sb.append("<td class=name>");
    sb.append("System: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (System.getProperty("java.version") != null)
        sb.append(System.getProperty("java.version"));
    sb.append(" ");
    if (System.getProperty("os.name") != null)
        sb.append(System.getProperty("os.name"));
    sb.append(" ");
    if (System.getProperty("os.version") != null)
        sb.append(System.getProperty("os.version"));
    sb.append(" ");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Plugin: ");
    sb.append("</td>");
    int r = 1;
    for (Iterator<String> i = Server.srv.pluginStore.keySet().iterator(); i.hasNext(); ) {
        String key = (String) i.next();
        sb.append("<td class=param>");
        sb.append(key);
        sb.append("</td>");
        r++;
        if (r >= 6) {
            r = 0;
            sb.append("</tr>");
            sb.append("<tr>");
            sb.append("<td class=param>");
            sb.append("</td>");
        }
    }
    for (Iterator<String> i = GroupManager.mgr.getGroupPlugins().keySet().iterator(); i.hasNext(); ) {
        String key = (String) i.next();
        sb.append("<td class=param>");
        sb.append(key);
        sb.append("</td>");
        r++;
        if (r >= 6) {
            r = 0;
            sb.append("</tr>");
            sb.append("<tr>");
            sb.append("<td class=param>");
            sb.append("</td>");
        }
    }
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("maxUsers: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MAX_USERS);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Usertimeout: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.USER_TIMEOUT == -1)
        sb.append("<img src =\"../static/no.gif\">");
    else
        sb.append(Server.srv.USER_TIMEOUT / 60000 + " min");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Userawaytimeout: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.USER_AWAY_TIMEOUT == -1)
        sb.append("<img src =\"../static/no.gif\">");
    else
        sb.append(Server.srv.USER_AWAY_TIMEOUT / 60000 + " min");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Viptimeout: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.VIP_TIMEOUT == -1)
        sb.append("<img src =\"../static/no.gif\">");
    else if (Server.srv.VIP_TIMEOUT == 0)
        sb.append("same as User");
    else
        sb.append(Server.srv.VIP_TIMEOUT / 60000 + " min");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("VipAwaytimeout: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.VIP_AWAY_TIMEOUT == -1) {
        sb.append("<img src =\"../static/no.gif\">");
    } else if (Server.srv.VIP_AWAY_TIMEOUT == 0) {
        sb.append("same as User");
    } else {
        sb.append(Server.srv.VIP_AWAY_TIMEOUT / 60000 + " min");
    }
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Max Suusers: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MAX_SUUSERS_PER_STARTGROUP);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Max Groupnamelength: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.MAX_GROUPNAME_LENGTH == -1)
        sb.append("<img src =\"../static/no.gif\">");
    else
        sb.append(Server.srv.MAX_GROUPNAME_LENGTH);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Max Groupthemelength: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.MAX_GROUPTHEME_LENGTH == -1)
        sb.append("<img src =\"../static/no.gif\">");
    else
        sb.append(Server.srv.MAX_GROUPTHEME_LENGTH);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Max Banduration: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MAX_BAN_DURATION + " min");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Max Floodbanduration: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.FLOOD_BAN_DURATION + " millis");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Floodprotectmillis: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.FLOOD_PROTECT_MILLIS);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Floodprotecttoleranc: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.FLOOD_PROTECT_TOLERANC);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Max Toolbanduration: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.TOOL_BAN_DURATION + " millis");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Toolprotectcounter: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.TOOL_PROTECT_COUNTER);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Toolprotecttoleranc: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.TOOL_PROTECT_TOLERANC);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Toolprotectminmills: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.TOOL_PROTECT_MINMILLS);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Toolprotectmincounter: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.TOOL_PROTECT_MINCOUNTER);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Max Su Banduration: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.MAX_SU_BAN_DURATION == -1)
        sb.append("<img src =\"../static/no.gif\">");
    else
        sb.append(Server.srv.MAX_SU_BAN_DURATION).append(" min");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Punish Duration: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.PUNISH_DURATION == -1)
        sb.append("<img src =\"../static/no.gif\">");
    else
        sb.append(Server.srv.PUNISH_DURATION / 1000).append(" sec");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Lock Duration: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.MAX_FLOCK_DURATION == -1) {
        sb.append("<img src =\"../static/no.gif\">");
    } else {
        sb.append(Server.srv.MAX_FLOCK_DURATION).append(" min");
    }
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<td class=name>");
    sb.append("<b>Use Trafficmonitor:</b> ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.USE_TRAFFIC_MONITOR)
        sb.append("<img src =\"../static/ok.gif\">");
    else
        sb.append("<img src =\"../static/no.gif\">");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("maxRequestsPerProxy:");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MAX_REQUESTS_PER_PROXY_IP);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("maxRequestsPerIP:");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MAX_REQUESTS_PER_IP);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Startgroups/Theme: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    String sgroups = Server.srv.props.getProperty("startgroups");
    String sgNames[] = sgroups.split(",");
    for (int i = 0; i < sgNames.length; i++) {
        int pos = sgNames[i].indexOf("/");
        String[] c = sgNames[i].split("/");
        String key = c[0].trim().toLowerCase();
        if (key.equals("exil")) {
            continue;
        }
        if (Server.srv.USE_PLUGINS && Server.srv.serverPlugin != null) {
            IServerPlugin[] svp = Server.srv.serverPlugin;
            if (svp != null) {
                String gName = null;
                for (int s = 0; s < svp.length; s++) {
                    try {
                        gName = svp[s].convertGroupname(key);
                    } catch (Exception e) {
                        Server.debug(svp[s], "catched exception from plugin", e, Server.MSG_ERROR, Server.LVL_MINOR);
                    }
                }
                sb.append(gName);
            }
        } else {
            sb.append(key);
        }
        if (c.length > 1) {
            String theme = sgNames[i].substring(pos + 1);
            if (Server.srv.USE_PLUGINS && Server.srv.serverPlugin != null) {
                String gTheme = null;
                IServerPlugin[] svp = Server.srv.serverPlugin;
                if (svp != null) {
                    for (int s = 0; s < svp.length; s++) {
                        try {
                            gTheme = svp[s].convertGroutheme(theme);
                        } catch (Exception e) {
                            Server.debug(svp[s], "catched exception from plugin", e, Server.MSG_ERROR, Server.LVL_MINOR);
                        }
                    }
                    sb.append("(").append(gTheme).append(")");
                }
            } else {
                sb.append("(").append(theme).append(")");
            }
        }
        sb.append("</td>");
        if (i < sgNames.length - 1) {
            sb.append("<td class=param>");
        }
    }
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("<b>use Plugins: </>");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.USE_PLUGINS) {
        sb.append("<img src =\"../static/ok.gif\">");
    } else {
        sb.append("<img src =\"../static/no.gif\">");
    }
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("<b>use BBC: </>");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.USE_BBC)
        sb.append("<img src =\"../static/ok.gif\">");
    else
        sb.append("<img src =\"../static/no.gif\">");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("bbcConvertGroupname: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.BBC_CONVERT_GROUPNAME)
        sb.append("<img src =\"../static/ok.gif\">");
    else
        sb.append("<img src =\"../static/no.gif\">");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("bbcConvertGrouptheme: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.BBC_CONVERT_GROUPTHEME)
        sb.append("<img src =\"../static/ok.gif\">");
    else
        sb.append("<img src =\"../static/no.gif\">");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("maxBBCTags: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MAX_BBCTAGS);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("minBbc<b>Font</b>RightEntrace: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MIN_BBC_FONT_RIGHT_ENTRACE);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("minBbc<b>Font</b>RightSepa: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MIN_BBC_FONT_RIGHT_SEPA);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("minBbc<b>B</b>RightEntrace: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MIN_BBC_B_RIGHT_ENTRACE);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("minBbc<b>B</b>RightSepa: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MIN_BBC_B_RIGHT_SEPA);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("minBbc<b>I</b>RightEntrace: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MIN_BBC_I_RIGHT_ENTRACE);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("minBbc<b>I</b>RightSepa: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MIN_BBC_I_RIGHT_SEPA);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("minBbc<b>U</b>RightEntrace: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MIN_BBC_U_RIGHT_ENTRACE);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("minBbc<b>U</b>RightSepa: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.MIN_BBC_U_RIGHT_SEPA);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("<b>canDelLogs: </>");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.CAN_DEL_LOGS)
        sb.append("<img src =\"../static/ok.gif\">");
    else
        sb.append("<img src =\"../static/no.gif\">");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("logfileDelhour: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.LOGFILE_DELHOUR);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("logfileDeldays: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.LOGFILE_DELDAYS);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("<b>Use Fadecolor:</b> ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.USE_FADECOLOR)
        sb.append("<img src =\"../static/ok.gif\">");
    else
        sb.append("<img src =\"../static/no.gif\">");
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("<b>Colorlock: </b>");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.COLOR_LOCK_MODE == 0)
        sb.append("<img src =\"../static/no.gif\">");
    else
        sb.append(Server.srv.COLOR_LOCK_MODE);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Locklevel: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    sb.append(Server.srv.COLOR_LOCK_LEVEL);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("<tr>");
    sb.append("<td class=name>");
    sb.append("Fadelocklevel: ");
    sb.append("</td>");
    sb.append("<td class=param>");
    if (Server.srv.FADECOLOR_LOCK_LEVEL == -1)
        sb.append("<img src =\"../static/no.gif\">");
    else
        sb.append(Server.srv.FADECOLOR_LOCK_LEVEL);
    sb.append("</td>");
    sb.append("</tr>");
    sb.append("</table>");
}
