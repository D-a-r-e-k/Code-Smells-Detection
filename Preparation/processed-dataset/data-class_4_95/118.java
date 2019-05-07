/**
	 * @since 1.3.3
	 */
private void processServerHooks(int event) {
    if (listOfServerHooks == null) {
        logger.warning("listOfServerHooks was null!");
        return;
    }
    ServerHook serverHook = null;
    boolean result = false;
    Iterator iterator = listOfServerHooks.iterator();
    String hooktype = "UNKNOWN";
    switch(event) {
        case ServerHook.PRE_STARTUP:
            hooktype = "PRE_STARTUP";
            break;
        case ServerHook.POST_STARTUP:
            hooktype = "POST_STARTUP";
            break;
        case ServerHook.PRE_SHUTDOWN:
            hooktype = "PRE_SHUTDOWN";
            break;
        case ServerHook.POST_SHUTDOWN:
            hooktype = "POST_SHUTDOWN";
            break;
    }
    while (iterator.hasNext()) {
        serverHook = (ServerHook) iterator.next();
        try {
            result = serverHook.handleEvent(event);
        } catch (Exception e) {
            result = false;
            logger.warning("Error invoking " + hooktype + " hook [" + serverHook.getClass().getName() + "]: " + e.getMessage());
        }
        logger.fine("Invoked " + hooktype + " hook [" + serverHook.getClass().getName() + "] was: " + result);
    }
}
