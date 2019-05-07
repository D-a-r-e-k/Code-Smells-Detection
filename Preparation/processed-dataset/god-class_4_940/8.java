private Group checkProperties(File f, Group g) {
    if (g == null)
        return null;
    Properties props = new Properties();
    try {
        FileInputStream in = new FileInputStream(f);
        props.load(in);
        in.close();
    } catch (FileNotFoundException fnfe) {
        return g;
    } catch (IOException ioe) {
        Server.debug(this, "crateByProperties:", ioe, Server.MSG_ERROR, Server.LVL_HALT);
        return g;
    }
    HashMap<String, Properties> map = new HashMap<String, Properties>();
    for (Iterator<Object> i = props.keySet().iterator(); i.hasNext(); ) {
        String key = i.next().toString();
        String low = key.toLowerCase();
        String val = props.getProperty(key);
        if ("timelock".equals(low)) {
            if (!g.hasState(IGroupState.MODERATED))
                return null;
            try {
                int sec = Integer.parseInt(val);
                g.setTimelockSec(sec);
            } catch (NumberFormatException nfe) {
                Server.log(g, "Timelock value isn't a number", Server.MSG_ERROR, Server.LVL_MINOR);
            }
        } else if ("membershiproom".equals(low)) {
            String[] memberships = val.toLowerCase().split(",");
            Vector<Membership> msObjects = new Vector<Membership>();
            for (int j = 0; j < memberships.length; j++) {
                Membership cms = MembershipManager.instance.getMembership(memberships[j]);
                if (cms == null)
                    continue;
                msObjects.add(cms);
            }
            g.setMembershipRoom((Membership[]) msObjects.toArray(new Membership[0]));
        } else if ("autosulist".equals(low)) {
            g.unsetAutoSu();
            g.setAutoSu(val.split(","));
        } else if ("autosumembershiplist".equals(low)) {
            String[] memberships = val.toLowerCase().split(",");
            Vector<Membership> msObjects = new Vector<Membership>();
            for (int j = 0; j < memberships.length; j++) {
                Membership cms = MembershipManager.instance.getMembership(memberships[j]);
                if (cms == null)
                    continue;
                msObjects.add(cms);
            }
            g.setAutoSuMembership((Membership[]) msObjects.toArray(new Membership[0]));
        } else if ("lockprotected".equals(low) && "true".equals(val)) {
            if (!g.hasState(IGroupState.OPEN))
                g.setState(IGroupState.OPEN);
            g.setState(IGroupState.LOCKPROTECTED);
        } else if ("autosu".equals(low) && "false".equals(val)) {
            g.unsetState(IGroupState.AUTO_SU_FIRST);
        } else if ("allowsu".equals(low) && "false".equals(val)) {
            g.unsetState(IGroupState.ALLOW_SU);
        } else if ("nosufirst".equals(low) && "true".equals(val)) {
            g.setState(IGroupState.NO_SU_FIRST);
        } else if ("minuserrolesu".equals(low)) {
            if ("vip".equals(val))
                g.setMinRightSu(IUserStates.ROLE_VIP);
            else if ("moderator".equals(val))
                g.setMinRightSu(IUserStates.IS_MODERATOR);
            else if ("admin".equals(val))
                g.setMinRightSu(IUserStates.ROLE_GOD);
            else
                Server.log(this, "minuserrolesu has wrong value '" + val + "' for group " + g.getRawName(), Server.MSG_STATE, Server.LVL_MINOR);
        } else if ("membershipsu".equals(low)) {
            String[] memberships = val.toLowerCase().split(",");
            Vector<Membership> msObjects = new Vector<Membership>();
            for (int j = 0; j < memberships.length; j++) {
                Membership cms = MembershipManager.instance.getMembership(memberships[j]);
                if (cms == null)
                    continue;
                msObjects.add(cms);
            }
            g.setMembershipSu((Membership[]) msObjects.toArray(new Membership[0]));
        } else if ("soundprooffor".equals(low)) {
            String[] roles = val.toLowerCase().split(",");
            for (int j = 0; j < roles.length; j++) {
                String curr = roles[j].trim();
                if ("vip".equals(curr))
                    g.setState(IGroupState.SND_PRF_VIP);
                else if ("guest".equals(curr))
                    g.setState(IGroupState.SND_PRF_GUEST);
                else if ("moderator".equals(curr))
                    g.setState(IGroupState.SND_PRF_MODERATOR);
                else if ("admin".equals(curr))
                    g.setState(IGroupState.SND_PRF_GOD);
                else if ("user".equals(curr))
                    g.setState(IGroupState.SND_PRF_USER);
            }
        } else if ("suforbidden".equals(low)) {
            g.setSuForbiddenMembership(val);
        } else if ("memberroom".equals(low)) {
            String[] memberships = val.toLowerCase().split(",");
            Vector<Membership> msObjects = new Vector<Membership>();
            for (int j = 0; j < memberships.length; j++) {
                Membership cms = MembershipManager.instance.getMembership(memberships[j]);
                if (cms == null)
                    continue;
                msObjects.add(cms);
            }
            g.setMemberRoom((Membership[]) msObjects.toArray(new Membership[0]));
        } else if ("allowusebbcodes".equals(low) && "true".equals(val)) {
            g.setState(IGroupState.ALLOW_USE_BBCODES);
        } else if ("allowusebbcodes".equals(low) && "false".equals(val)) {
            g.unsetState(IGroupState.ALLOW_USE_BBCODES);
        } else if ("allowusesmiley".equals(low) && "true".equals(val)) {
            g.setState(IGroupState.ALLOW_USE_SMILEY);
        } else if ("allowusesmiley".equals(low) && "false".equals(val)) {
            g.unsetState(IGroupState.ALLOW_USE_SMILEY);
        } else if ("deactivatehitdice".equals(low) && "true".equals(val)) {
            g.setState(IGroupState.DEACTIVATE_HITDICE);
        } else if ("sucanban".equals(low) && "true".equals(val)) {
            g.setState(IGroupState.SU_CAN_BAN);
        } else if ("sucanban".equals(low) && "false".equals(val)) {
            g.unsetState(IGroupState.SU_CAN_BAN);
        } else if ("sucansettheme".equals(low) && "true".equals(val)) {
            g.setState(IGroupState.SU_CAN_SETTHEME);
        } else if ("sucansettheme".equals(low) && "false".equals(val)) {
            g.unsetState(IGroupState.SU_CAN_SETTHEME);
        } else if ("cansetpunishable".equals(low) && "true".equals(val)) {
            g.setState(IGroupState.CAN_SET_PUNISHABLE);
        } else if ("notallowjoinunreg".equals(low) && "true".equals(val)) {
            g.setState(IGroupState.NOT_ALLOW_JOIN_UNREG);
        } else if (low.startsWith("extension.")) {
            String namespace = low.substring(10);
            int idx = namespace.indexOf(".");
            if (idx == -1) {
                Server.log(this, "invalid GroupPolicy for extension: " + low, Server.MSG_ERROR, Server.LVL_MINOR);
                continue;
            }
            String propName = namespace.substring(idx + 1);
            namespace = namespace.substring(0, idx);
            Properties p = (Properties) map.get(namespace);
            if (p == null) {
                p = new Properties();
                map.put(namespace, p);
            }
            p.setProperty(propName, val);
        }
    }
    if (map.size() > 0) {
        Vector<IGroupPlugin> plugins = new Vector<IGroupPlugin>();
        for (Iterator<String> i = map.keySet().iterator(); i.hasNext(); ) {
            String namespace = (String) i.next();
            Properties p = (Properties) map.get(namespace);
            String url = p.getProperty("url");
            Object o;
            synchronized (GroupManager.pluginStore) {
                o = GroupManager.pluginStore.get(url);
                if (o == null) {
                    try {
                        Class<?> piClass = Class.forName(url);
                        Method getInstance = piClass.getMethod("getMasterInstance", null);
                        if (getInstance == null)
                            throw new Exception("Specified plugin-object doesn't implement static getMasterInstance");
                        o = getInstance.invoke(null, null);
                        if (!(o instanceof IGroupPlugin))
                            throw new Exception("Specified plugin-object doesn't implement interface IGroupPlugin");
                        GroupManager.pluginStore.put(url, o);
                    } catch (Exception e) {
                        Server.log(this, "invalid url for extension: (" + e + ") " + url, Server.MSG_ERROR, Server.LVL_MINOR);
                        continue;
                    }
                }
            }
            try {
                plugins.add(((IGroupPlugin) o).instanceForGroup(namespace, g, p));
            } catch (Exception e) {
                Server.debug(this, "catched exception while getting GroupPlugin-instance", e, Server.MSG_STATE, Server.LVL_MAJOR);
            }
        }
        g.setPlugins((IGroupPlugin[]) plugins.toArray(new IGroupPlugin[0]));
    }
    return g;
}
