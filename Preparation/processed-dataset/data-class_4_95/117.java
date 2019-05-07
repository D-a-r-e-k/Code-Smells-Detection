/**
	 * @since 1.3.3
	 */
private void loadServerHooksClasses() {
    if (getServerHooks() == null)
        return;
    listOfServerHooks = new ArrayList();
    ServerHook serverHook = null;
    String serverHookClassName = null;
    Class serverHookClass = null;
    //add system hooks  
    serverHook = new GhostSocketReaper();
    serverHook.initHook(QuickServer.this);
    listOfServerHooks.add(serverHook);
    ghostSocketReaper = (GhostSocketReaper) serverHook;
    //add user hooks if any  
    Iterator iterator = getServerHooks().iterator();
    while (iterator.hasNext()) {
        serverHookClassName = (String) iterator.next();
        try {
            serverHookClass = getClass(serverHookClassName, true);
            serverHook = (ServerHook) serverHookClass.newInstance();
            serverHook.initHook(QuickServer.this);
            listOfServerHooks.add(serverHook);
            logger.info("Loaded server hook: " + serverHookClassName);
            logger.fine("Server hook info: " + serverHook.info());
        } catch (Exception e) {
            logger.warning("Could not load server hook [" + serverHookClassName + "]: " + e);
            logger.fine("StackTrace:\n" + MyString.getStackTrace(e));
        }
    }
}
