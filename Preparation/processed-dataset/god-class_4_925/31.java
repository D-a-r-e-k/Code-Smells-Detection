private TheClient initTheClient() {
    TheClient theClient = new TheClient();
    theClient.setServer(QuickServer.this);
    theClient.setTimeoutMsg(getTimeoutMsg());
    theClient.setMaxAuthTry(getMaxAuthTry());
    //v1.2  
    theClient.setMaxAuthTryMsg(getMaxAuthTryMsg());
    theClient.setClientEventHandler(clientEventHandler);
    theClient.setClientExtendedEventHandler(clientExtendedEventHandler);
    //v1.4.6  
    theClient.setClientCommandHandler(clientCommandHandler);
    theClient.setClientObjectHandler(clientObjectHandler);
    //v1.2  
    theClient.setClientBinaryHandler(clientBinaryHandler);
    //v1.4  
    theClient.setClientWriteHandler(clientWriteHandler);
    //v1.4.5  
    theClient.setAuthenticator(authenticator);
    //v1.3  
    theClient.setClientAuthenticationHandler(clientAuthenticationHandler);
    //v1.4.6  
    theClient.setTimeout(socketTimeout);
    theClient.setMaxConnectionMsg(maxConnectionMsg);
    theClient.setCommunicationLogging(getCommunicationLogging());
    //v1.3.2  
    return theClient;
}
