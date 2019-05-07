/**
	 * the main-method of server is responsible for:
	 * .) reading in the configuration
	 * .) initializing the server
	 * .) starting the neccessary threads
	 * 
	 * If this task's are completed this main-thread will
	 * be used to remove bans which are not valid anymore
	 * @param args the base-path may be given @commandline. Usage: freecs.Server -b=[path to mainfolder]
	 */
public static void main(String args[]) {
    for (int i = 0; i < args.length; i++) {
        if (args[i].startsWith("-b=")) {
            BASE_PATH = args[0].substring(3);
        } else if (args[i].equals("printcharsets")) {
            System.out.println("Available Charsets:");
            Set<String> ks = Charset.availableCharsets().keySet();
            for (Iterator<String> it = ks.iterator(); it.hasNext(); ) {
                System.out.print(":> ");
                System.out.println((String) it.next());
            }
            System.exit(0);
        }
    }
    if (BASE_PATH == null)
        BASE_PATH = "./";
    srv = new Server();
    srv.readConfig();
    srv.initServer();
    srv.startThreads();
    if (srv.USE_CENTRAL_REQUESTQUEUE)
        Server.log("Server", "starting up with CENTRAL-requestqueue", Server.MSG_STATE, Server.LVL_MAJOR);
    else
        Server.log("Server", "starting up with per RequestReader-requestqueue", Server.MSG_STATE, Server.LVL_MAJOR);
    long lastMessage = 0;
    while (srv.isRunning()) {
        /*            if (Server.DEBUG || lastMessage + 5000 > System.currentTimeMillis()) {
                Server.log ("Server", "loopstart", Server.MSG_STATE, Server.LVL_VERY_VERBOSE);
                lastMessage = System.currentTimeMillis();
            } */
        try {
            long now = System.currentTimeMillis();
            long rws[][] = RequestReader.getWorkingSince();
            boolean b[] = RequestReader.getAliveState();
            StringBuffer sb = new StringBuffer("ThreadsWorkingTime:");
            for (int i = 0; i < rws.length; i++) {
                if (rws[i][0] == 0) {
                    sb.append(" 0, ");
                } else {
                    sb.append(" ");
                    sb.append(now - rws[i][0]);
                    sb.append(", ");
                }
                if (b[i])
                    sb.append("alive@");
                else
                    sb.append("dead@");
                switch((short) rws[i][1]) {
                    case RequestReader.WAITING:
                        sb.append("waiting");
                        continue;
                    case RequestReader.EVAL_GET_MESSAGES_APND2WRITE:
                        sb.append("appending message to writequeue");
                        continue;
                    case RequestReader.EVAL_GET_MESSAGES_SND_MSGS:
                        sb.append("sending scheduled message");
                        continue;
                    case RequestReader.EVAL_GET_MESSAGES:
                        sb.append("sending messages-frame");
                        continue;
                    case RequestReader.EVAL_GET_STATE:
                        sb.append("retrieving /state");
                        continue;
                    case RequestReader.EVAL_GET:
                        sb.append("evaluating getrequest");
                        continue;
                    case RequestReader.EVAL_POST:
                        sb.append("evaluating postrequest");
                        continue;
                    case RequestReader.EVAL_POST_LOGIN:
                        sb.append("loging in");
                        continue;
                    case RequestReader.EVAL_PREP4SEND:
                        sb.append("perparing for sending");
                        continue;
                    case RequestReader.EVAL_SEND:
                        sb.append("evaluating a /SEND request");
                        continue;
                    case RequestReader.EVAL_SENDFINAL:
                        sb.append("sending content");
                        continue;
                    case RequestReader.EVALUATE_COMMAND:
                        sb.append("evaluating a command");
                        String cmd = RequestReader.getCurrCommant(i);
                        if (cmd != null)
                            sb.append(" (").append(cmd).append(")");
                        continue;
                    case RequestReader.EVALUATING:
                        sb.append("evaluating");
                        continue;
                    case RequestReader.PARSE_MSG:
                        sb.append("parsing message");
                        continue;
                    case RequestReader.READING:
                        sb.append("reading");
                        continue;
                    case RequestReader.EVAL_POST_LOGIN_RESULT:
                        sb.append("evaluating login-result");
                        continue;
                    case RequestReader.TRYLOGIN:
                        sb.append("trylogin");
                        continue;
                    case RequestReader.TRYLOGIN_AUTHENTICATE:
                        sb.append("trylogin authenticate");
                        continue;
                    case RequestReader.TRYLOGIN_CHECK_FRIENDS:
                        sb.append("trylogin check friends");
                        continue;
                    case RequestReader.TRYLOGIN_CHECK4PRESENCE:
                        sb.append("trylogin check for presence");
                        continue;
                    case RequestReader.TRYLOGIN_CORRECT_PERMISSION:
                        sb.append("trylogin correct permission");
                        continue;
                    case RequestReader.TRYLOGIN_SCHEDULE_FRIENDMSGS:
                        sb.append("trylogin schedule online-friends-messages");
                        continue;
                    case RequestReader.TRYLOGIN_SCHEDULE_VIPMSG:
                        sb.append("trylogin schedule vip-message");
                        continue;
                    case RequestReader.TRYLOGIN_SEND_LOGINMSG:
                        sb.append("trylogin send loginmessages");
                        continue;
                    case RequestReader.TRYLOGIN_SET_GROUP:
                        sb.append("trylogin set group");
                        continue;
                    case RequestReader.TRYLOGIN_SET_PERMISSION:
                        sb.append("trylogin set permission");
                        continue;
                }
            }
            Server.log("static Server", sb.toString(), MSG_STATE, LVL_VERBOSE);
            Runtime r = Runtime.getRuntime();
            long free = r.freeMemory();
            long total = r.totalMemory();
            long max = r.maxMemory();
            long used = r.totalMemory() - r.freeMemory();
            sb = new StringBuffer();
            sb.append("Memory-Report (VM-MaxSize/VM-CurrSize/free/used): ");
            sb.append(max).append("/");
            sb.append(total).append("/");
            sb.append(free).append("/");
            sb.append(used);
            Server.log(null, sb.toString(), MSG_STATE, LVL_MINOR);
            // Cleaning up the banlist is triggered here  
            long lVal = now + 30001;
            for (Enumeration<String> e = srv.banList.keys(); e.hasMoreElements(); ) {
                Object key = e.nextElement();
                BanObject bObj = (BanObject) srv.banList.get(key);
                if (bObj == null)
                    continue;
                if (bObj.bannedBy.equals("Config( parmaBannedIp )"))
                    continue;
                if (bObj.time < now) {
                    if (checkLogLvl(Server.MSG_STATE, Server.LVL_MINOR)) {
                        sb = new StringBuffer("Server: removing ban for ").append(key);
                        Server.log("static Server", sb.toString(), MSG_STATE, LVL_MINOR);
                    }
                    srv.banList.remove(key);
                } else if (bObj.time < lVal)
                    lVal = bObj.time;
            }
            // Cleaning up the storelist is triggered here  
            lVal = now + 30001;
            for (Enumeration<ActionstoreObject> e = srv.storeList.keys(); e.hasMoreElements(); ) {
                Object key = e.nextElement();
                ActionstoreObject sObj = (ActionstoreObject) key;
                if (sObj == null)
                    continue;
                if (sObj.time < now) {
                    if (checkLogLvl(Server.MSG_STATE, Server.LVL_MINOR)) {
                        sb = new StringBuffer("Server: removing store for ");
                        sb.append(sObj.usr);
                        sb.append("(");
                        sb.append(sObj.action);
                        sb.append(")");
                        Server.log("static Server", sb.toString(), MSG_STATE, LVL_MINOR);
                    }
                    srv.storeList.remove(key);
                    if (sObj.equalsActionState(IActionStates.SUBAN)) {
                        Group g = GroupManager.mgr.getGroup(sObj.room);
                        User u = UserManager.mgr.getUserByName(sObj.usr);
                        if (g != null) {
                            if (u != null) {
                                MessageParser mp = new MessageParser();
                                mp.setMessageTemplate("message.uban.server");
                                mp.setTargetGroup(g);
                                u.sendMessage(mp);
                                g.setBanForUser(u.getName(), false);
                            } else
                                g.setBanForUser(sObj.usr, false);
                        }
                    }
                    if (sObj.equalsActionState(IActionStates.FLOCKCOL)) {
                        User u = UserManager.mgr.getUserByName(sObj.usr);
                        if (u != null)
                            u.setCollock(false);
                    }
                    if (sObj.equalsActionState(IActionStates.FLOCKAWAY)) {
                        User u = UserManager.mgr.getUserByName(sObj.usr);
                        if (u != null)
                            u.setAwaylock(false);
                    }
                    if (sObj.equalsActionState(IActionStates.FLOCKME)) {
                        User u = UserManager.mgr.getUserByName(sObj.usr);
                        if (u != null)
                            u.setActlock(false);
                    }
                    sObj.clearObject();
                } else if (sObj.time < lVal)
                    lVal = sObj.time;
            }
            long slpTime = lVal - now;
            if (slpTime < 30)
                slpTime = 30;
            Thread.sleep(slpTime);
        } catch (Exception ie) {
        }
    }
}
