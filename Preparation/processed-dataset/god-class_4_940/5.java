public void handle(IRequest req, ContentContainer c) throws AccessForbiddenException {
    checkAccessIp(req, c);
    checkAccessAuth(req, c);
    StringBuffer sb = new StringBuffer();
    renderTemplate(req, "admin_header", sb);
    StringBuffer action = null;
    if (req.getValue("do") != null)
        action = new StringBuffer(req.getValue("do"));
    else
        action = new StringBuffer();
    if ("removeuser".equalsIgnoreCase(action.toString())) {
        removeUser(req, sb);
    } else if ("removeright".equalsIgnoreCase(action.toString())) {
        removeRight(req, sb);
    } else if ("giveright".equalsIgnoreCase(action.toString())) {
        giveRight(req, sb);
    } else if ("unpunish".equalsIgnoreCase(action.toString())) {
        unpunishUser(req, sb);
    } else if ("changestate".equalsIgnoreCase(action.toString())) {
        changeState(req, sb);
    } else if ("refreshgp".equalsIgnoreCase(action.toString())) {
        refreshGp(req, sb);
    } else if ("removeban".equalsIgnoreCase(action.toString())) {
        removeBan(req, sb);
    } else if ("removeaction".equalsIgnoreCase(action.toString())) {
        removeActionstore(req, sb);
    } else if ("sendmessage".equalsIgnoreCase(action.toString())) {
        sendMessage(req, sb);
    } else if ("sendmessagetouser".equalsIgnoreCase(action.toString())) {
        sendMessageToUser(req, sb);
    } else if ("sendmessagetogroup".equalsIgnoreCase(action.toString())) {
        sendMessageToGroup(req, sb);
    } else if ("grouplist".equalsIgnoreCase(action.toString())) {
        renderGrouplist(req, sb);
    } else if ("userlist".equalsIgnoreCase(action.toString())) {
        renderUserlist(req, sb);
    } else if ("searchuser".equalsIgnoreCase(action.toString())) {
        searchUser(req, sb);
    } else if ("searchgroup".equalsIgnoreCase(action.toString())) {
        searchGroup(req, sb);
    } else if ("banlist".equalsIgnoreCase(action.toString())) {
        renderBanlist(req, sb);
    } else if ("actionstorelist".equalsIgnoreCase(action.toString())) {
        renderActionstorelist(req, sb);
    } else if ("configoverview".equalsIgnoreCase(action.toString())) {
        renderConfigoverview(req, sb);
    } else if ("shutdown".equalsIgnoreCase(action.toString())) {
        // Server.srv.startShutdown();  
        if (slevel() == 1) {
            SelectionKey key = req.getKey();
            InetAddress ia = null;
            try {
                SocketChannel sc = (SocketChannel) key.channel();
                ia = sc.socket().getInetAddress();
            } catch (Exception e) {
                Server.debug(this, "" + ia.toString(), e, Server.MSG_STATE, Server.LVL_MAJOR);
                throw new AccessForbiddenException(true);
            }
            Server.log("[Admin]", "Server shutdown " + ia.getHostAddress(), Server.MSG_STATE, Server.LVL_MAJOR);
            System.exit(0);
        } else {
            sb.append("Access denied.");
        }
    } else {
        renderTemplate(req, "admin_index", sb);
        TemplateSet ts = Server.srv.templatemanager.getTemplateSet("admin");
        Template tpl = ts.getTemplate("admin_index");
        if (tpl == null) {
            standartIndex(req, sb);
        }
    }
    sb.append("</body></html>");
    c.wrap(sb.toString(), req.getCookieDomain());
}
