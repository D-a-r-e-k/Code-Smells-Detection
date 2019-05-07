void method0() { 
private String timeoutMsg;
private String maxAuthTryMsg;
private int maxAuthTry;
private Socket socket;
private Authenticator authenticator;
private ClientAuthenticationHandler clientAuthenticationHandler;
//v1.4.6  
private ClientEventHandler eventHandler;
//v1.4.6  
private ClientExtendedEventHandler extendedEventHandler;
//v1.4.6  
private ClientCommandHandler commandHandler;
private ClientObjectHandler objectHandler;
//v1.2  
private ClientBinaryHandler binaryHandler;
//v1.4  
private QuickServer quickServer;
private ClientData clientData;
//--v1.3.2  
private boolean trusted = false;
private boolean communicationLogging = true;
//--v1.4.5  
private int socketTimeout;
private String maxConnectionMsg;
private ClientEvent event = ClientEvent.RUN_BLOCKING;
private SocketChannel socketChannel;
private ClientWriteHandler writeHandler;
}
