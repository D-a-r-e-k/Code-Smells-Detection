public void logoutUser(User u) throws Exception {
    try {
        if (dbp.readOnly || dbp.updCols == null || dbp.updCols.length < 1)
            return;
        PreparedStatement ps = getUpdate();
        for (int i = 0; i < dbp.updCols.length; i++) {
            String cname = dbp.updNames[i];
            if ("chattime".equalsIgnoreCase(cname)) {
                ps.setLong(i + 1, u.getChattime());
            } else if ("userrights".equalsIgnoreCase(cname)) {
                ps.setInt(i + 1, u.getPermissionMap());
            } else if ("fadecolor".equalsIgnoreCase(cname)) {
                ps.setString(i + 1, u.getFadeColCode());
            } else if ("bgcolor".equalsIgnoreCase(cname)) {
                ps.setString(i + 1, u.getBgColCode());
            } else if ("color".equalsIgnoreCase(cname)) {
                ps.setString(i + 1, u.getColCode());
            } else if ("friends".equalsIgnoreCase(cname)) {
                StringBuffer sb = new StringBuffer();
                for (Enumeration<?> e = u.friends(); e.hasMoreElements(); ) {
                    String s = (String) e.nextElement();
                    sb.append(s);
                    if (e.hasMoreElements())
                        sb.append(", ");
                }
                ps.setString(i + 1, sb.toString());
            } else if ("ignorelist".equalsIgnoreCase(cname)) {
                StringBuffer sb = new StringBuffer();
                for (Enumeration<String> e = u.ignoreList(); e.hasMoreElements(); ) {
                    String s = (String) e.nextElement();
                    sb.append(s);
                    if (e.hasMoreElements())
                        sb.append(", ");
                }
                ps.setString(i + 1, sb.toString());
            } else if ("notifyfriends".equalsIgnoreCase(cname)) {
                int idx = dbp.nameV.indexOf("notifyfriends");
                switch(dbp.types[idx]) {
                    case Types.BIGINT:
                    case Types.BIT:
                    case Types.DECIMAL:
                    case Types.INTEGER:
                    case Types.SMALLINT:
                        ps.setInt(i + 1, u.notifyFriends());
                        break;
                    case Types.BOOLEAN:
                        ps.setBoolean(i + 1, u.notifyFriends() == User.FN_ALL ? true : false);
                        break;
                    default:
                        ps.setString(i + 1, u.notifyFriends() == User.FN_ALL ? "true" : "false");
                }
            } else if ("extratitle".equalsIgnoreCase(cname)) {
                ps.setString(i + 1, u.getCustomTitle());
            } else if ("cookie".equalsIgnoreCase(cname)) {
                // and overwrite it with "not_logged_in" when the user loggs out  
                ps.setString(i + 1, "not_logged_in");
            } else if ("blocked".equalsIgnoreCase(cname)) {
                int idx = dbp.nameV.indexOf("blocked");
                switch(dbp.types[idx]) {
                    case Types.BIGINT:
                    case Types.BIT:
                    case Types.DECIMAL:
                    case Types.INTEGER:
                    case Types.SMALLINT:
                        ps.setInt(i + 1, u.blocked ? 1 : 0);
                        break;
                    case Types.BOOLEAN:
                        ps.setBoolean(i + 1, u.blocked);
                        break;
                    default:
                        ps.setString(i + 1, u.blocked ? "1" : "0");
                }
            } else if ("activated".equalsIgnoreCase(cname)) {
                int idx = dbp.nameV.indexOf("activated");
                switch(dbp.types[idx]) {
                    case Types.BIGINT:
                    case Types.BIT:
                    case Types.DECIMAL:
                    case Types.INTEGER:
                    case Types.SMALLINT:
                        ps.setInt(i + 1, u.activated ? 1 : 0);
                        break;
                    case Types.BOOLEAN:
                        ps.setBoolean(i + 1, u.activated);
                        break;
                    default:
                        ps.setString(i + 1, u.activated ? "1" : "0");
                }
            } else {
                Server.log(this, "save custom Property " + cname, Server.MSG_AUTH, Server.LVL_VERBOSE);
                int idx = dbp.nameV.indexOf(cname);
                switch(dbp.types[idx]) {
                    case Types.BIGINT:
                    case Types.BIT:
                    case Types.DECIMAL:
                    case Types.INTEGER:
                    case Types.SMALLINT:
                    case Types.BOOLEAN:
                    default:
                        ps.setObject(i + 1, u.getProperty(cname));
                }
            }
        }
        if (dbp.idField != null) {
            if (u.getID() == null) {
                Server.log(u, "Unable to store logout-data for " + u.getName() + " because of missing id-value", Server.MSG_AUTH, Server.LVL_MAJOR);
                return;
            }
            ps.setString(dbp.updCols.length + 1, u.getID());
        } else {
            ps.setString(dbp.updCols.length + 1, u.getName().toLowerCase());
        }
        int rows = ps.executeUpdate();
        sCnt++;
        if (rows == 1) {
            con.commit();
        } else if (rows < 1) {
            Server.log(Thread.currentThread(), this.toString() + "LOGOUT unable to update userdata! No record for: " + dbp.idField != null ? dbp.idField + " = " + u.getID() : "username = " + u.getName().toLowerCase(), Server.MSG_AUTH, Server.LVL_MAJOR);
            return;
        } else if (rows > 1) {
            Server.log(Thread.currentThread(), this.toString() + "LOGOUT unable to update userdata! More than one value would be updated: (" + dbp.idField != null ? dbp.idField + " = " + u.getID() : "username = " + u.getName().toLowerCase() + ")", Server.MSG_AUTH, Server.LVL_MAJOR);
            try {
                con.rollback();
                Server.log(Thread.currentThread(), this.toString() + "LOGOUT rollback successfully", Server.MSG_AUTH, Server.LVL_VERBOSE);
            } catch (SQLException se) {
                Server.log(Thread.currentThread(), this.toString() + "LOGOUT rollback failed!!!", Server.MSG_AUTH, Server.LVL_MAJOR);
            }
        }
        checkWarnings(ps, "logoutUser");
    } catch (Exception e) {
        isValid = false;
        release();
        throw e;
    }
}
