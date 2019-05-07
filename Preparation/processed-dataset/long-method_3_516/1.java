private void readColumns(User u, ResultSet rs) throws SQLException {
    for (int i = 0; i < dbp.columns.length; i++) {
        String cname = dbp.names[i];
        int idx = i + 1;
        if ("userright".equals(cname)) {
            String val = rs.getString(idx);
            if (val == null || val.length() == 0 || "null".equalsIgnoreCase(val)) {
                u.setPermission(IUserStates.ROLE_USER);
            } else if ("true".equalsIgnoreCase(val) || "yes".equalsIgnoreCase(val) || "vip".equalsIgnoreCase(val)) {
                u.setPermission(IUserStates.ROLE_VIP);
            } else if ("admin".equalsIgnoreCase(val)) {
                u.setPermission(IUserStates.ROLE_GOD);
            } else if ("moderator".equalsIgnoreCase(val)) {
                u.setPermission(IUserStates.ROLE_VIP | IUserStates.IS_MODERATOR);
            } else if ("guest".equalsIgnoreCase(val)) {
                u.setPermission(IUserStates.IS_GUEST);
            } else if ("asshole".equalsIgnoreCase(val)) {
                u.setPermission(IUserStates.ROLE_ASSHOLE);
            } else {
                try {
                    u.setPermission(Integer.parseInt(val));
                } catch (NumberFormatException nfe) {
                    Server.log(Thread.currentThread(), this.toString() + "LOGIN userrights-column contains unknown value, corrected to ROLE_USER\r\n(must be null/true,yes,vip or VIP/admin/moderator/guest/user/assohle) ", Server.MSG_AUTH, Server.LVL_MAJOR);
                    u.setPermission(IUserStates.ROLE_USER);
                }
            }
        } else if ("id".equals(cname)) {
            u.setID(rs.getString(idx));
        } else if ("color".equals(cname)) {
            u.setColCode(rs.getString(idx));
        } else if ("fadecolor".equals(cname)) {
            Server.srv.USE_FADECOLOR = true;
            if (rs.getString(idx) != null) {
                u.setFadeColCode(rs.getString(idx));
            }
        } else if ("bgcolor".equals(cname)) {
            Server.srv.USE_BGCOLOR = true;
            if (rs.getString(idx) != null) {
                u.setBgColCode(rs.getString(idx));
            }
        } else if ("chattime".equals(cname)) {
            u.setProperty("chattime", new Long(rs.getLong(idx)));
        } else if ("lastlogin".equals(cname)) {
            switch(dbp.types[i]) {
                case Types.BIGINT:
                case Types.INTEGER:
                case Types.NUMERIC:
                case Types.SMALLINT:
                    u.setProperty("lastlogin", new Timestamp(rs.getLong(idx)));
                    break;
                case Types.DATE:
                case Types.TIMESTAMP:
                    Timestamp ts = rs.getTimestamp(idx);
                    u.setProperty("lastlogin", ts);
                    break;
                default:
                    String s = rs.getString(idx);
                    if (rs.wasNull()) {
                        u.setProperty("lastlogin", new Timestamp(System.currentTimeMillis()));
                        break;
                    }
                    try {
                        long l = Long.parseLong(s);
                        u.setProperty("lastlogin", new Timestamp(l));
                    } catch (NumberFormatException nfe) {
                        try {
                            u.setProperty("lastlogin", Timestamp.valueOf(s));
                        } catch (IllegalArgumentException iae) {
                            Server.log(this, "LOGIN Unable to retrieve lastlogin-value! " + s, Server.MSG_AUTH, Server.LVL_MAJOR);
                        }
                    }
            }
        } else if ("friends".equals(cname)) {
            List<?> users = pool.authenticator.parseUserList(rs.getString(idx));
            for (Iterator<?> it = users.iterator(); it.hasNext(); ) {
                u.addFriend((String) it.next());
            }
        } else if ("ignorelist".equals(cname)) {
            List<?> ignores = pool.authenticator.parseUserList(rs.getString(idx));
            for (Iterator<?> it = ignores.iterator(); it.hasNext(); ) {
                u.ignoreUser((String) it.next());
            }
        } else if ("notifyfriends".equals(cname)) {
            switch(dbp.types[i]) {
                case Types.BIGINT:
                case Types.INTEGER:
                case Types.NUMERIC:
                case Types.SMALLINT:
                    u.setFriendsNotification(rs.getShort(idx));
                    break;
                default:
                    u.setFriendsNotification(pool.authenticator.parseBoolean(rs.getString(idx)) ? Server.srv.FN_DEFAULT_MODE_TRUE : Server.srv.FN_DEFAULT_MODE_FALSE);
            }
        } else if ("customtitle".equals(cname)) {
            u.setCustomTitle(rs.getString(idx));
        } else if ("blocked".equals(cname)) {
            u.blocked = pool.authenticator.parseBoolean(rs.getString(idx));
        } else if ("activated".equals(cname)) {
            u.activated = pool.authenticator.parseBoolean(rs.getString(idx));
        } else {
            String strg = getEncodedString(rs, idx);
            u.setProperty(cname, strg);
        }
    }
}
