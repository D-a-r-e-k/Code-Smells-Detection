/**
	 * Loads all the Business Logic class
	 * @since 1.4.6
	 */
protected void loadBusinessLogic() throws Exception {
    if (clientCommandHandlerString == null && clientEventHandlerString == null) {
        logger.severe("ClientCommandHandler AND ClientEventHandler was not set.");
        throw new AppException("ClientCommandHandler AND ClientEventHandler was not set.");
    }
    clientCommandHandler = null;
    if (clientCommandHandlerString != null) {
        logger.finest("Loading ClientCommandHandler class..");
        Class clientCommandHandlerClass = getClass(clientCommandHandlerString, true);
        clientCommandHandler = (ClientCommandHandler) clientCommandHandlerClass.newInstance();
    }
    boolean setClientCommandHandlerLookup = false;
    clientEventHandler = null;
    if (clientEventHandlerString == null) {
        clientEventHandlerString = "org.quickserver.net.server.impl.DefaultClientEventHandler";
        setClientCommandHandlerLookup = true;
    }
    logger.finest("Loading ClientEventHandler class..");
    if (clientEventHandlerString.equals(clientCommandHandlerString) && ClientEventHandler.class.isInstance(clientCommandHandler)) {
        clientEventHandler = (ClientEventHandler) clientCommandHandler;
    } else {
        clientEventHandler = (ClientEventHandler) getClass(clientEventHandlerString, true).newInstance();
        if (setClientCommandHandlerLookup) {
            ((DefaultClientEventHandler) clientEventHandler).setClientCommandHandler(clientCommandHandler);
        }
    }
    clientExtendedEventHandler = null;
    if (clientExtendedEventHandlerString != null) {
        logger.finest("Loading ClientExtendedEventHandler class..");
        if (clientExtendedEventHandlerString.equals(clientCommandHandlerString) && ClientExtendedEventHandler.class.isInstance(clientCommandHandler)) {
            clientExtendedEventHandler = (ClientExtendedEventHandler) clientCommandHandler;
        } else if (clientExtendedEventHandlerString.equals(clientEventHandlerString) && ClientExtendedEventHandler.class.isInstance(clientEventHandler)) {
            clientExtendedEventHandler = (ClientExtendedEventHandler) clientEventHandler;
        } else {
            Class clientExtendedEventHandlerClass = getClass(clientExtendedEventHandlerString, true);
            clientExtendedEventHandler = (ClientExtendedEventHandler) clientExtendedEventHandlerClass.newInstance();
        }
    }
    clientObjectHandler = null;
    if (clientObjectHandlerString != null) {
        logger.finest("Loading ClientObjectHandler class..");
        if (clientObjectHandlerString.equals(clientCommandHandlerString) && ClientObjectHandler.class.isInstance(clientCommandHandler)) {
            clientObjectHandler = (ClientObjectHandler) clientCommandHandler;
        } else if (clientObjectHandlerString.equals(clientEventHandlerString) && ClientObjectHandler.class.isInstance(clientEventHandler)) {
            clientObjectHandler = (ClientObjectHandler) clientEventHandler;
        } else if (clientObjectHandlerString.equals(clientExtendedEventHandlerString) && ClientObjectHandler.class.isInstance(clientExtendedEventHandler)) {
            clientObjectHandler = (ClientObjectHandler) clientExtendedEventHandler;
        } else {
            clientObjectHandler = (ClientObjectHandler) getClass(clientObjectHandlerString, true).newInstance();
        }
    }
    //end of != null  
    clientBinaryHandler = null;
    if (clientBinaryHandlerString != null) {
        logger.finest("Loading ClientBinaryHandler class..");
        if (clientBinaryHandlerString.equals(clientCommandHandlerString) && ClientBinaryHandler.class.isInstance(clientCommandHandler)) {
            clientBinaryHandler = (ClientBinaryHandler) clientCommandHandler;
        } else if (clientBinaryHandlerString.equals(clientEventHandlerString) && ClientBinaryHandler.class.isInstance(clientEventHandler)) {
            clientBinaryHandler = (ClientBinaryHandler) clientEventHandler;
        } else if (clientBinaryHandlerString.equals(clientExtendedEventHandlerString) && ClientBinaryHandler.class.isInstance(clientExtendedEventHandler)) {
            clientBinaryHandler = (ClientBinaryHandler) clientExtendedEventHandler;
        } else if (clientBinaryHandlerString.equals(clientObjectHandlerString) && ClientBinaryHandler.class.isInstance(clientObjectHandler)) {
            clientBinaryHandler = (ClientBinaryHandler) clientObjectHandler;
        } else {
            clientBinaryHandler = (ClientBinaryHandler) getClass(clientBinaryHandlerString, true).newInstance();
        }
    }
    //end of != null  
    clientWriteHandler = null;
    if (clientWriteHandlerString != null) {
        logger.finest("Loading ClientWriteHandler class..");
        if (clientWriteHandlerString.equals(clientCommandHandlerString) && ClientWriteHandler.class.isInstance(clientCommandHandler)) {
            clientWriteHandler = (ClientWriteHandler) clientCommandHandler;
        } else if (clientWriteHandlerString.equals(clientEventHandlerString) && ClientWriteHandler.class.isInstance(clientEventHandler)) {
            clientWriteHandler = (ClientWriteHandler) clientEventHandler;
        } else if (clientWriteHandlerString.equals(clientExtendedEventHandlerString) && ClientWriteHandler.class.isInstance(clientExtendedEventHandler)) {
            clientWriteHandler = (ClientWriteHandler) clientExtendedEventHandler;
        } else if (clientWriteHandlerString.equals(clientObjectHandlerString) && ClientWriteHandler.class.isInstance(clientObjectHandler)) {
            clientWriteHandler = (ClientWriteHandler) clientObjectHandler;
        } else if (clientWriteHandlerString.equals(clientBinaryHandlerString) && ClientWriteHandler.class.isInstance(clientBinaryHandler)) {
            clientWriteHandler = (ClientWriteHandler) clientBinaryHandler;
        } else {
            clientWriteHandler = (ClientWriteHandler) getClass(clientWriteHandlerString, true).newInstance();
        }
    }
    //end of != null  
    Class authenticatorClass = null;
    if (clientAuthenticationHandlerString != null) {
        logger.finest("Loading ClientAuthenticationHandler class..");
        authenticatorClass = getClass(clientAuthenticationHandlerString, true);
    }
    if (authenticatorClass != null) {
        Object obj = authenticatorClass.newInstance();
        if (ClientAuthenticationHandler.class.isInstance(obj))
            clientAuthenticationHandler = (ClientAuthenticationHandler) obj;
        else
            authenticator = (Authenticator) obj;
    }
    clientDataClass = null;
    if (clientDataString != null) {
        logger.finest("Loading ClientData class..");
        clientDataClass = getClass(clientDataString, true);
    }
    Assertion.affirm(clientEventHandler != null, "ClientEventHandler was not loaded!");
}
