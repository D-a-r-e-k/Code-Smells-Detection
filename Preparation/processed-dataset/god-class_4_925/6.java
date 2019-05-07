/**
	 * Starts the QuickServer.
	 *
	 * @exception org.quickserver.net.AppException 
	 *  if Server already running or if it could not load the classes
	 *  [ClientCommandHandler, ClientAuthenticationHandler, ClientData].
	 * @see #startService
	 */
public void startServer() throws AppException {
    logger.fine("Starting " + getName());
    if (isClosed() == false) {
        logger.warning("Server " + getName() + " already running.");
        throw new AppException("Server " + getName() + " already running.");
    }
    if (serverBanner == null) {
        serverBanner = "\n-------------------------------" + "\n Name : " + getName() + "\n Port : " + getPort() + "\n-------------------------------\n";
        logger.finest("Default Server Banner Generated");
    }
    try {
        loadApplicationClasses();
        //load class from Advanced Settings  
        Class clientIdentifierClass = getClass(getBasicConfig().getAdvancedSettings().getClientIdentifier(), true);
        clientIdentifier = (ClientIdentifier) clientIdentifierClass.newInstance();
        clientIdentifier.setQuickServer(QuickServer.this);
        //load class from ObjectPoolConfig  
        Class poolManagerClass = getClass(getBasicConfig().getObjectPoolConfig().getPoolManager(), true);
        poolManager = (PoolManager) poolManagerClass.newInstance();
        //load class QSObjectPoolMaker  
        Class qsObjectPoolMakerClass = getClass(getBasicConfig().getAdvancedSettings().getQSObjectPoolMaker(), true);
        qsObjectPoolMaker = (QSObjectPoolMaker) qsObjectPoolMakerClass.newInstance();
        loadServerHooksClasses();
        processServerHooks(ServerHook.PRE_STARTUP);
        if (getSecure().isLoad() == true)
            loadSSLContext();
        //v1.4.0  
        loadBusinessLogic();
    } catch (ClassNotFoundException e) {
        logger.severe("Could not load class/s : " + e.getMessage());
        throw new AppException("Could not load class/s : " + e.getMessage());
    } catch (InstantiationException e) {
        logger.severe("Could not instantiate class/s : " + e.getMessage());
        throw new AppException("Could not instantiate class/s : " + e.getMessage());
    } catch (IllegalAccessException e) {
        logger.severe("Illegal access to class/s : " + e.getMessage());
        throw new AppException("Illegal access to class/s : " + e.getMessage());
    } catch (IOException e) {
        logger.severe("IOException : " + e.getMessage());
        logger.fine("StackTrace:\n" + MyString.getStackTrace(e));
        throw new AppException("IOException : " + e.getMessage());
    } catch (Exception e) {
        logger.severe("Exception : " + e.getMessage());
        logger.fine("StackTrace:\n" + MyString.getStackTrace(e));
        throw new AppException("Exception : " + e);
    }
    //v1.3.3  
    if (getSecurityManagerClass() != null) {
        System.setSecurityManager(getSecurityManager());
    }
    blockingMode = getBasicConfig().getServerMode().getBlocking();
    setServiceState(Service.INIT);
    t = new Thread(this, "QuickServer - " + getName());
    t.start();
    do {
        Thread.yield();
    } while (getServiceState() == Service.INIT);
    if (getServiceState() != Service.RUNNING) {
        if (exceptionInRun != null)
            throw new AppException("Could not start server " + getName() + "! Details: " + exceptionInRun);
        else
            throw new AppException("Could not start server " + getName());
    }
    lastStartTime = new java.util.Date();
    logger.fine("Started " + getName() + ", Date: " + lastStartTime);
}
