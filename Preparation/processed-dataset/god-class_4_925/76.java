/**
	 * Configures QSAdminServer based on the passed QuickServerConfig object.
	 * @since 1.2
	 */
public void configQuickServer(QSAdminServerConfig config) throws Exception {
    QuickServer qs = getQSAdminServer().getServer();
    qs.setBasicConfig(config);
    //set the Logging Level to same as main QS  
    String temp = getConsoleLoggingLevel();
    //config.getConsoleLoggingLevel();  
    configConsoleLoggingLevel(qs, temp);
    //set the Logging Formatter to same as main QS  
    //qs.setConsoleLoggingFormatter(config.getConsoleLoggingFormatter());  
    qs.setConsoleLoggingFormatter(getConsoleLoggingFormatter());
    qs.setClientEventHandler(config.getClientEventHandler());
    //v1.4.6  
    qs.setClientCommandHandler(config.getClientCommandHandler());
    qs.setName(config.getName());
    qs.setPort(config.getPort());
    if (config.getAuthenticator() != null)
        qs.setAuthenticator(config.getAuthenticator());
    else if (config.getClientAuthenticationHandler() != null)
        qs.setClientAuthenticationHandler(config.getClientAuthenticationHandler());
    //v1.4.6  
    qs.setClientObjectHandler(config.getClientObjectHandler());
    qs.setClientBinaryHandler(config.getClientBinaryHandler());
    //v1.4  
    qs.setClientWriteHandler(config.getClientWriteHandler());
    //v1.4.5  
    qs.setClientData(config.getClientData());
    qs.setClientExtendedEventHandler(config.getClientExtendedEventHandler());
    //v1.4.6  
    qs.setDefaultDataMode(config.getDefaultDataMode());
    //v1.4.6  
    qs.setServerBanner(config.getServerBanner());
    qs.setTimeout(config.getTimeout());
    qs.setMaxAuthTry(config.getMaxAuthTry());
    qs.setMaxAuthTryMsg(config.getMaxAuthTryMsg());
    qs.setTimeoutMsg(config.getTimeoutMsg());
    qs.setMaxConnection(config.getMaxConnection());
    qs.setMaxConnectionMsg(config.getMaxConnectionMsg());
    qs.setBindAddr(config.getBindAddr());
    //v1.3.2  
    qs.setCommunicationLogging(config.getCommunicationLogging());
    getQSAdminServer().setCommandPlugin(config.getCommandPlugin());
    //v1.3.2  
    if (config.getCommandShellEnable().equals("true"))
        getQSAdminServer().setShellEnable(true);
    getQSAdminServer().setPromptName(config.getCommandShellPromptName());
    //v1.3.3  
    qs.setAccessConstraintConfig(config.getAccessConstraintConfig());
    qs.setServerHooks(config.getServerHooks());
    qs.setSecure(config.getSecure());
}
