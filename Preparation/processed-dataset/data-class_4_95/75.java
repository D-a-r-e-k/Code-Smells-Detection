/**
	 * Configures QuickServer based on the passed QuickServerConfig object.
	 * @since 1.2
	 */
public void configQuickServer(QuickServerConfig config) throws Exception {
    QuickServer qs = QuickServer.this;
    qs.setConfig(config);
    //v1.3  
    qs.setBasicConfig(config);
    String temp = config.getConsoleLoggingLevel();
    configConsoleLoggingLevel(qs, temp);
    temp = null;
    qs.setConsoleLoggingFormatter(config.getConsoleLoggingFormatter());
    qs.setName(config.getName());
    qs.setPort(config.getPort());
    qs.setClientEventHandler(config.getClientEventHandler());
    qs.setClientCommandHandler(config.getClientCommandHandler());
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
    //v1.3.3  
    qs.setSecurityManagerClass(config.getSecurityManagerClass());
    qs.setAccessConstraintConfig(config.getAccessConstraintConfig());
    temp = config.getApplicationJarPath();
    if (temp != null) {
        File ajp = new File(temp);
        if (ajp.isAbsolute() == false) {
            temp = config.getConfigFile();
            ajp = new File(temp);
            temp = ajp.getParent() + File.separatorChar + config.getApplicationJarPath();
            config.setApplicationJarPath(temp);
            temp = null;
        }
        qs.setApplicationJarPath(config.getApplicationJarPath());
        //set path also to QSAdmin  
        if (config.getQSAdminServerConfig() != null) {
            getQSAdminServer().getServer().setApplicationJarPath(config.getApplicationJarPath());
        }
    }
    qs.setServerHooks(config.getServerHooks());
    qs.setSecure(config.getSecure());
}
