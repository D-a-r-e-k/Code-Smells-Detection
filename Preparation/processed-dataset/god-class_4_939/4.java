/**
	 * checks for properties in the config-file to overrule the default-values
	 * this has to be "reloadable" meaning that values may be changed on the fly
	 * @see freecs.util.FileMonitor
	 * @see freecs.interfaces.IReloadable
	 */
private synchronized void checkForConfigValues() {
    String sgroups = props.getProperty("startgroups");
    if (sgroups == null)
        Server.log(this, "No starting-rooms are deffined: startrooms=[room1/TITLE1 [, room2/TITLE2, ...", MSG_ERROR, LVL_HALT);
    GroupManager.mgr.updateStartingGroups(sgroups.split(","));
    Server.log(this, "updating log-destinations", MSG_CONFIG, LVL_MINOR);
    LOGFILE[MSG_CONFIG] = checkProperty("logfileCfg", "console");
    LOGFILE[MSG_AUTH] = checkProperty("logfileAuth", "console");
    LOGFILE[MSG_STATE] = checkProperty("logfileState", "console");
    LOGFILE[MSG_TRAFFIC] = checkProperty("logfileTraffic", "console");
    LOGFILE[MSG_ERROR] = checkProperty("logfileError", "console");
    LOGFILE[MSG_MESSAGE] = checkProperty("logfileMessage", "console");
    if (LOGFILE[MSG_MESSAGE] != null && !LOGFILE[MSG_MESSAGE].equals("console")) {
        File f = new File(LOGFILE[MSG_MESSAGE]);
        if (!f.exists()) {
            f.mkdirs();
        }
    }
    LOG_QUEUE_SIZE = checkProperty("logQueueSize", 500);
    try {
        // startup logwriter  
        LogWriter l = LogWriter.instance;
    } catch (Exception e) {
        e.printStackTrace();
    }
    Server.log(this, "Reading config...", MSG_CONFIG, LVL_MINOR);
    READBUFFER_SIZE = checkProperty("readbuffer", 640);
    READER_MAX_IDLETIME = checkProperty("threadMaxIdletime", 30000);
    READER_MAX_QUEUE = checkProperty("ioQueueSize", 5);
    FLOOD_PROTECT_TOLERANC = checkProperty("floodProtectTolerance", 3);
    FLOOD_PROTECT_MILLIS = checkProperty("floodProtectMillis", 500);
    FLOOD_BAN_DURATION = checkProperty("floodBanDuration", 30000);
    TOOL_PROTECT_TOLERANC = checkProperty("toolProtectTolerance", 250);
    TOOL_PROTECT_COUNTER = checkProperty("toolProtectCounter", 10);
    TOOL_BAN_DURATION = checkProperty("toolBanDuration", 600) * 60000;
    TOOL_PROTECT_MINMILLS = checkProperty("toolProtectMinmills", 12000);
    TOOL_PROTECT_MINCOUNTER = checkProperty("toolProtectMincounter", 10);
    JOIN_PUNISHED_COUNTER = checkProperty("joinpunishedcounter", 2);
    COLOR_CHANGE_INTERVAL = checkProperty("colorChangeInterval", 15000);
    MESSAGE_FLOOD_INTERVAL = checkProperty("messageFloodInterval", 5000);
    USER_TIMEOUT = checkProperty("userTimeout", 15) * 60000;
    if (USER_TIMEOUT < 0) {
        USER_TIMEOUT = 15 * 60000;
        Server.log(this, "WARNING Usrtimeout < 0 setting Standarttimeout", Server.MSG_CONFIG, Server.LVL_MAJOR);
    }
    USER_AWAY_TIMEOUT = checkProperty("userAwayTimeout", 30) * 60000;
    if (USER_AWAY_TIMEOUT < 0) {
        Server.log(this, "WARNING UsrAwayTimeout < 0 deactivated", Server.MSG_CONFIG, Server.LVL_MAJOR);
    }
    USER_REMOVE_SCHEDULE_TIME = checkProperty("userRemoveDelay", 2000);
    TCP_RECEIVE_BUFFER_WINDOW = checkProperty("tcpReceiveBuffer", 4096);
    FILE_CHECK_INTERVAL = checkProperty("fileCheckInterval", 10000);
    if (FILE_CHECK_INTERVAL < 1000)
        FILE_CHECK_INTERVAL = 1000;
    CAN_DEL_LOGS = checkProperty("canDelLogs", false);
    LOGFILE_DELHOUR = checkProperty("logfileDelhour", 1);
    LOGFILE_DELDAYS = checkProperty("logfileDeldays", 2);
    if (LOGFILE_DELDAYS < 2)
        LOGFILE_DELDAYS = 2;
    String ndcs = checkProperty("charset", "iso-8859-1");
    if (!ndcs.equals(DEFAULT_CHARSET)) {
        defaultCs = Charset.forName(ndcs);
        defaultCsEnc = defaultCs.newEncoder();
        DEFAULT_CHARSET = ndcs;
    }
    int cookied = 0;
    String cookiedomain = props.getProperty("cookieDomain");
    if (cookiedomain != null) {
        StringBuffer cookie = new StringBuffer();
        String cd[] = cookiedomain.split(",");
        for (int i = 0; i < cd.length; i++) {
            cookied++;
            cookie.append(cd[i].trim().toLowerCase()).append(",");
            Server.log("[Server]", "CookieDomain Configured: " + cd[i].trim().toLowerCase(), Server.MSG_CONFIG, Server.LVL_MAJOR);
        }
        COOKIE_DOMAIN = new StringBuffer(cookie);
    }
    String servername = props.getProperty("server");
    if (servername != null) {
        int servern = 0;
        Vector<String> server = new Vector<String>();
        String sv[] = servername.split(",");
        for (int i = 0; i < sv.length; i++) {
            servern++;
            server.addElement(sv[i].trim().toLowerCase());
            Server.log("[Server]", "Server Configured: " + sv[i].trim().toLowerCase(), Server.MSG_CONFIG, Server.LVL_MAJOR);
        }
        SERVER_NAME = new Vector<String>(server);
        if (servern > 1)
            if (cookied < servern || cookied > servern)
                Server.log(this, "Server Halt:CookieDomain(" + cookied + ")<>Servername(" + servern + ")", Server.MSG_ERROR, Server.LVL_HALT);
    }
    DEFAULT_MEMBERSHIP = checkProperty("defaultMembership", "standart");
    USE_SMILEY = checkProperty("useSmiley", false);
    SMILEY_PER_LINE = checkProperty("SmileyPerLine", 5);
    SMILEY_SERVER = checkProperty("SmileysDir", "/static");
    BLOCKED_NICK_AUTOHARDKICK = checkProperty("blockedNickAutohardkick", false);
    USE_PLUGINS = checkProperty("usePlugins", false);
    USE_BBC = checkProperty("useBBC", false);
    BBC_CONVERT_GROUPNAME = checkProperty("bbcConvertGroupname", false);
    BBC_CONVERT_GROUPTHEME = checkProperty("bbcConvertGrouptheme", false);
    MAX_BBCTAGS = checkProperty("maxBBCTags", 1);
    if (MAX_BBCTAGS < 1 || MAX_BBCTAGS > 5)
        MAX_BBCTAGS = 1;
    MIN_BBC_FONT_RIGHT_ENTRACE = checkProperty("minBbcFontRightEntrace", "user");
    MIN_BBC_FONT_RIGHT_SEPA = checkProperty("minBbcFontRightSepa", "user");
    MIN_BBC_B_RIGHT_ENTRACE = checkProperty("minBbcBRightEntrace", "user");
    MIN_BBC_B_RIGHT_SEPA = checkProperty("minBbcBRightSepa", "user");
    MIN_BBC_I_RIGHT_ENTRACE = checkProperty("minBbcIRightEntrace", "user");
    MIN_BBC_I_RIGHT_SEPA = checkProperty("minBbcIRightSepa", "user");
    MIN_BBC_U_RIGHT_ENTRACE = checkProperty("minBbcURightEntrace", "user");
    MIN_BBC_U_RIGHT_SEPA = checkProperty("minBbcURightSepa", "user");
    ALLOW_EXTERNAL = checkProperty("allowExternalLogin", true);
    USE_CENTRAL_REQUESTQUEUE = checkProperty("useCentralRequestqueue", false);
    DEBUG_TEMPLATESET = checkProperty("debugTemplateset", false);
    STRICT_HOST_BINDING = checkProperty("useStrictHostBinding", true);
    MAX_READERS = checkProperty("maxThreads", 100);
    USE_HTTP11 = checkProperty("useHTTP1.1", true);
    MAX_USERS = checkProperty("maxUsers", 2000);
    TOUCH_USER_DELAY = checkProperty("touchUserDelay", 20000);
    MAX_BAN_DURATION = checkProperty("maxBanDuration", 120);
    USE_IP_BAN = checkProperty("useIpBan", true);
    DEFAULT_BAN_DURATION = checkProperty("defaultBanDuration", 10);
    INITIAL_RESPONSE_QUEUE = checkProperty("responseQueueSize", 100);
    MAX_RESPONSE_QUEUE = checkProperty("maxResponseQueueSize", 1000);
    MAX_REQUESTS_PER_PROXY_IP = checkProperty("maxRequestsPerProxy", 20000);
    MAX_REQUESTS_PER_IP = checkProperty("maxRequestsPerIp", 90);
    HOST_BAN_DURATION = checkProperty("floodHostBanDuration", 3600000);
    USE_TRAFFIC_MONITOR = checkProperty("useTrafficMonitor", false);
    MAX_DIE_NUMBER = checkProperty("maximumDieNumber", 10);
    MAX_DIE_EYES = checkProperty("maximumDieEyes", 20);
    MAX_SUUSERS_PER_STARTGROUP = checkProperty("maxSuPerStartgroup", 5);
    USE_TOKENSTORE = checkProperty("useTokenedLogin", false);
    MD5_PASSWORDS = checkProperty("MD5EncodePasswords", false);
    MAX_USERNAME_LENGTH = checkProperty("maxUserNameLength", 30);
    MAX_GROUPNAME_LENGTH = checkProperty("maxGroupNameLength", -1);
    MAX_GROUPTHEME_LENGTH = checkProperty("maxGroupThemeLength", -1);
    MESSAGE_FLOOD_LENGHT = checkProperty("messageFloodLength", -1);
    USE_MESSAGE_RENDER_CACHE = checkProperty("useMessageRenderCache", false);
    VIP_TIMEOUT = checkProperty("vipTimeout", 0) * 60000;
    VIP_AWAY_TIMEOUT = checkProperty("vipAwayTimeout", 0) * 60000;
    MAX_MCALL_KEY = checkProperty("maxMcallKey", 30);
    MAX_PMSTORE = checkProperty("maxPmstore", 0);
    String httpUname = checkProperty("admin.http.username", null);
    if (httpUname != null)
        ADMIN_HTTP_USERNAME = httpUname.split(",");
    String httpPassword = checkProperty("admin.http.password", null);
    if (httpPassword != null)
        ADMIN_HTTP_PASSWORD = httpPassword.split(",");
    String sLevel = checkProperty("admin.http.securitylevel", null);
    if (sLevel != null)
        ADMIN_HTTP_SECLEVEL = sLevel.split(",");
    ADMIN_HTTP_ALLOWED = checkProperty("admin.http.allowedClients", "");
    ADMIN_XMLRPC_PORT = checkProperty("admin.xmlrpc.port", 0);
    ADMIN_XMLRPC_ALLOWED = checkProperty("admin.xmlrpc.allowedClients", "");
    UNAME_PREFIX_GOD = checkProperty("prefix.admin", "<b>");
    UNAME_SUFFIX_GOD = checkProperty("suffix.admin", "(A)</b>");
    UNAME_PREFIX_GUEST = checkProperty("prefix.guest", "");
    UNAME_SUFFIX_GUEST = checkProperty("suffix.guest", "(G)");
    UNAME_PREFIX_MODERATOR = checkProperty("prefix.moderator", "");
    UNAME_SUFFIX_MODERATOR = checkProperty("suffix.moderator", "(M)");
    UNAME_PREFIX_PUNISHED = checkProperty("prefix.punished", "<s>");
    UNAME_SUFFIX_PUNISHED = checkProperty("suffix.punished", "</s>");
    UNAME_PREFIX_SU = checkProperty("prefix.su", "<i>");
    UNAME_SUFFIX_SU = checkProperty("suffix.su", "</i>");
    UNAME_PREFIX_VIP = checkProperty("prefix.vip", "<b>");
    UNAME_SUFFIX_VIP = checkProperty("suffix.vip", "</b>");
    READER_TIMEOUT = checkProperty("readerTimeout", 5000);
    LOGIN_TIMEOUT = checkProperty("loginTimeout", 20000);
    FN_DEFAULT_MODE_FALSE = (short) checkProperty("friendNotificationMode.false", 0);
    FN_DEFAULT_MODE_TRUE = (short) checkProperty("friendNotificationMode.true", 2);
    COLOR_LOCK_MODE = (short) checkProperty("colorLockMode", 0);
    COLOR_LOCK_LEVEL = (short) checkProperty("colorLockLevel", 1);
    if (COLOR_LOCK_LEVEL < 1 || COLOR_LOCK_LEVEL > 10)
        COLOR_LOCK_LEVEL = 1;
    FADECOLOR_LOCK_LEVEL = (short) checkProperty("fadecolorLockLevel", -1);
    if (FADECOLOR_LOCK_LEVEL == -1)
        FADECOLOR_LOCK_LEVEL = COLOR_LOCK_LEVEL;
    if (FADECOLOR_LOCK_LEVEL < 1 || FADECOLOR_LOCK_LEVEL > 10)
        FADECOLOR_LOCK_LEVEL = 1;
    KEEP_ALIVE_TIMEOUT = checkProperty("keepAliveTimeout", 30) * 1000;
    PUNISH_DURATION = checkProperty("punishBanDuration", -1);
    ALLOW_CHANGE_USERAGENT = checkProperty("allowChangeUseragent", true);
    MAX_FLOCK_DURATION = checkProperty("maxFlockDuration", -1);
    MAX_SU_BAN_DURATION = checkProperty("maxSuBanDuration", -1);
    DEFAULT_TEMPLATESET = checkProperty("defaultTemplateset", null);
    TRACE_CREATE_AND_FINALIZE = checkProperty("traceCreateAndFinalize", false);
    //Load Plugins  
    if (USE_PLUGINS) {
        String plugins = props.getProperty("plugins");
        if (plugins != null) {
            String values[] = plugins.split(",");
            Vector<String> pluginUrl = new Vector<String>();
            for (int i = 0; i < values.length; i++) pluginUrl.add(values[i].trim());
            loadPlugin(pluginUrl);
        } else {
            resetPluginStore();
        }
    } else {
        resetPluginStore();
    }
    //Load Commands  
    String url = props.getProperty("commandsUrl");
    if (url != null) {
        String values[] = url.split(",");
        Vector<String> jarUrl = new Vector<String>();
        for (int i = 0; i < values.length; i++) jarUrl.add(values[i].trim());
        loadCommands(jarUrl);
        CommandSet.getCommandSet().checkCommendSet();
    } else {
        Vector<String> jarUrl = new Vector<String>();
        jarUrl.add("lib/freecs.jar");
        loadCommands(jarUrl);
    }
    //Load xmlrpc Handler  
    String xmlRpcHandlerUrl = props.getProperty("xmlRpcHandlerUrl");
    if (xmlRpcHandlerUrl != null) {
        String values[] = xmlRpcHandlerUrl.split(",");
        Vector<String> jarUrl = new Vector<String>();
        for (int i = 0; i < values.length; i++) jarUrl.add(values[i].trim());
        loadXmlRpcHandler(jarUrl);
    }
    String val = props.getProperty("moderatedgroups");
    if (val != null) {
        String values[] = val.split(",");
        Vector<String> names = new Vector<String>();
        for (int i = 0; i < values.length; i++) names.add(values[i].trim().toLowerCase());
        GroupManager.mgr.updateModeratedGroups(names);
    } else {
        GroupManager.mgr.updateModeratedGroups(new Vector<String>());
    }
    val = props.getProperty("vips");
    if (val != null) {
        Vector<String> tvl = new Vector<String>();
        String values[] = val.split(",");
        for (int i = 0; i < values.length; i++) {
            tvl.addElement(values[i].trim().toLowerCase());
        }
        UserManager.mgr.updateVips(tvl);
    } else
        UserManager.mgr.updateVips(new Vector<String>());
    val = props.getProperty("admins");
    if (val != null) {
        Vector<String> tvl = new Vector<String>();
        String values[] = val.split(",");
        for (int i = 0; i < values.length; i++) {
            tvl.addElement(values[i].trim().toLowerCase());
        }
        UserManager.mgr.updateAdmins(tvl);
    } else
        UserManager.mgr.updateAdmins(new Vector<String>());
    val = props.getProperty("moderators");
    if (val != null) {
        Vector<String> tvl = new Vector<String>();
        String values[] = val.split(",");
        for (int i = 0; i < values.length; i++) {
            tvl.addElement(values[i].trim().toLowerCase());
        }
        UserManager.mgr.updateModerators(tvl);
    } else
        UserManager.mgr.updateModerators(new Vector<String>());
    val = props.getProperty("guests");
    if (val != null) {
        Vector<String> tvl = new Vector<String>();
        String values[] = val.split(",");
        for (int i = 0; i < values.length; i++) {
            tvl.addElement(values[i].trim().toLowerCase());
        }
        UserManager.mgr.updateGuests(tvl);
    } else
        UserManager.mgr.updateGuests(new Vector<String>());
    val = srv.ADMIN_HTTP_ALLOWED;
    if (val != null) {
        String hsts[] = val.split(",");
        Vector<InetAddress> newHsts = new Vector<InetAddress>();
        for (int i = 0; i < hsts.length; i++) {
            try {
                InetAddress ia = InetAddress.getByName(hsts[i].trim());
                if (!newHsts.contains(ia))
                    newHsts.addElement(ia);
            } catch (Exception e) {
                StringBuffer tsb = new StringBuffer("Server.checkForConfigValues: unable to add adminHost ");
                tsb.append(hsts[i]);
                Server.debug(this, tsb.toString(), e, Server.MSG_ERROR, Server.LVL_MAJOR);
            }
        }
        Vector<InetAddress> remove = (Vector) adminHosts.clone();
        remove.removeAll(newHsts);
        newHsts.removeAll(adminHosts);
        adminHosts.removeAll(remove);
        adminHosts.addAll(newHsts);
        for (Enumeration<String> tp = tempAdmins.elements(); tp.hasMoreElements(); ) {
            String u = (String) tp.nextElement();
            User nu = UserManager.mgr.getUserByName(u);
            if (nu != null)
                addTempAdminhost(nu);
        }
    }
    val = props.getProperty("permaBannedIp");
    if (val != null) {
        String hsts[] = val.split(",");
        Vector<InetAddress> newHsts = new Vector<InetAddress>();
        for (int i = 0; i < hsts.length; i++) {
            try {
                InetAddress ia = InetAddress.getByName(hsts[i].trim());
                if (!newHsts.contains(ia)) {
                    newHsts.addElement(ia);
                    permaBanHost(ia, "perma banned IP");
                }
            } catch (Exception e) {
                StringBuffer tsb = new StringBuffer("Server.checkForConfigValues: unable to add perma banned IP ");
                tsb.append(hsts[i]);
                Server.debug(this, tsb.toString(), e, Server.MSG_ERROR, Server.LVL_MAJOR);
            }
        }
    }
    String oldTimeZone = TIMEZONE;
    TIMEZONE = checkProperty("timezone", null);
    TimeZone tz = null;
    if (TIMEZONE == null || TIMEZONE.length() < 1) {
        StringBuffer sb = new StringBuffer("checkForConfigValues: setting TimeZone to default-TimeZone (");
        tz = TimeZone.getDefault();
        sb.append(tz.getID());
        sb.append(")");
        Server.log(this, sb.toString(), Server.MSG_STATE, Server.LVL_MINOR);
        if (tz.equals(cal.getTimeZone()))
            tz = null;
    } else if (oldTimeZone == null || !oldTimeZone.equals(TIMEZONE)) {
        try {
            tz = TimeZone.getTimeZone(TIMEZONE);
            if (tz.equals(cal.getTimeZone())) {
                Server.log(this, "checkForConfigVals: TimeZone has not changed", Server.MSG_ERROR, Server.LVL_MINOR);
                tz = null;
            } else if (!tz.getID().equals(TIMEZONE)) {
                StringBuffer sb = new StringBuffer("checkForConfigVals: TimeZone is set to ");
                sb.append(tz.getID());
                sb.append(" now. The following TimeZones are available:\r\n");
                String[] ids = TimeZone.getAvailableIDs();
                for (int i = 0; i < ids.length; i++) {
                    sb.append(ids[i]);
                    if (i < ids.length)
                        sb.append(", ");
                }
                Server.log(this, sb.toString(), Server.MSG_STATE, Server.LVL_MINOR);
            }
            StringBuffer sb = new StringBuffer("checkForConfigValues: setting TimeZone to ");
            sb.append(tz.getID());
            Server.log(this, sb.toString(), Server.MSG_STATE, Server.LVL_MINOR);
        } catch (Exception e) {
            Server.debug(this, "checkForConfigValues: unable to set TimeZone!", e, Server.MSG_ERROR, Server.LVL_MAJOR);
        }
    }
    if (tz != null) {
        cal.setTimeZone(tz);
    }
    Listener.updateSscRecieveBuffer(this.TCP_RECEIVE_BUFFER_WINDOW);
    val = checkProperty("allowedLoginHosts", null);
    if (val != null) {
        String hsts[] = val.split(",");
        Vector<InetAddress> newHsts = new Vector<InetAddress>();
        for (int i = 0; i < hsts.length; i++) {
            try {
                InetAddress ia = InetAddress.getByName(hsts[i].trim());
                if (!newHsts.contains(ia))
                    newHsts.addElement(ia);
            } catch (Exception e) {
                StringBuffer tsb = new StringBuffer("Server.checkForConfigValues: unable to add adminHost ");
                tsb.append(hsts[i]);
                Server.debug(this, tsb.toString(), e, Server.MSG_ERROR, Server.LVL_MAJOR);
            }
        }
        Vector<InetAddress> remove = (Vector) allowedLoginHosts.clone();
        remove.removeAll(newHsts);
        newHsts.removeAll(allowedLoginHosts);
        allowedLoginHosts.removeAll(remove);
        allowedLoginHosts.addAll(newHsts);
    }
    // set logmask and debug-switch  
    String logVal = props.getProperty("debug");
    if (logVal != null && logVal.equalsIgnoreCase("false"))
        DEBUG = false;
    else if (logVal != null && logVal.equalsIgnoreCase("true"))
        DEBUG = true;
    logVal = props.getProperty("log_config");
    if (logVal != null)
        LOG_MASK[MSG_CONFIG] = new Short(Short.parseShort(logVal, 10));
    logVal = props.getProperty("log_auth");
    if (logVal != null)
        LOG_MASK[MSG_AUTH] = new Short(Short.parseShort(logVal, 10));
    logVal = props.getProperty("log_state");
    if (logVal != null)
        LOG_MASK[MSG_STATE] = new Short(Short.parseShort(logVal, 10));
    logVal = props.getProperty("log_traffic");
    if (logVal != null)
        LOG_MASK[MSG_TRAFFIC] = new Short(Short.parseShort(logVal, 10));
    logVal = props.getProperty("log_error");
    if (logVal != null)
        LOG_MASK[MSG_ERROR] = new Short(Short.parseShort(logVal, 10));
    logVal = props.getProperty("log_message");
    if (logVal != null)
        LOG_MASK[MSG_MESSAGE] = new Short(Short.parseShort(logVal, 10));
    logVal = props.getProperty("log_sepamessage");
    if (logVal != null)
        LOG_MASK[MSG_SEPAMESSAGE] = new Short(Short.parseShort(logVal, 10));
}
